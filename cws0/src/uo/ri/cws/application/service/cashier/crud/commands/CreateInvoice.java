package uo.ri.cws.application.service.cashier.crud.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class CreateInvoice {
    private List<String> workOrderIds;

    public CreateInvoice(List<String> workOrderIds) {
        this.workOrderIds = workOrderIds;
    }

    public InvoiceDto execute() throws BusinessException {
        if (workOrderIds == null || workOrderIds.isEmpty()) throw new IllegalArgumentException("WorkOrderIds list cannot be null or empty");
        for (String id : workOrderIds) {
            if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("WorkOrder id cannot be null or empty");
        }

        try (Connection c = Jdbc.createThreadConnection()) {
            c.setAutoCommit(false); 
            try {
                checkWorkOrdersExist(c);
                checkWorkOrdersFinished(c);

                BigDecimal subtotal = calculateTotalAmount(c);
                long number = generateNextInvoiceNumber(c);
                LocalDate date = LocalDate.now();
                BigDecimal vatPercentage = getVatPercentage(date);
                
                BigDecimal vatAmount = subtotal.multiply(vatPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                BigDecimal total = subtotal.add(vatAmount).setScale(2, RoundingMode.HALF_UP);

                InvoiceDto invoice = new InvoiceDto();
                invoice.id = UUID.randomUUID().toString();
                invoice.number = number;
                invoice.date = date;
                
                // Mapeo exacto respetando al 100% el DTO del InvoicingService
                invoice.total = total;
                invoice.subtotal = subtotal;
                invoice.vatPercentage = vatPercentage;
                invoice.vatAmount = vatAmount;
                
                invoice.state = "ISSUED"; 
                invoice.version = 1L;

                insertInvoiceSafe(c, invoice);
                linkWorkOrders(c, invoice.id);
                
                c.commit();
                return invoice;
            } catch (SQLException | BusinessException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkWorkOrdersExist(Connection c) throws SQLException, BusinessException {
        try (PreparedStatement pst = c.prepareStatement("SELECT id FROM TWorkOrders WHERE id = ?")) {
            for (String id : workOrderIds) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) throw new BusinessException("Workorder does not exist");
                }
            }
        }
    }

    private void checkWorkOrdersFinished(Connection c) throws SQLException, BusinessException {
        try (PreparedStatement pst = c.prepareStatement("SELECT * FROM TWorkOrders WHERE id = ?")) {
            for (String id : workOrderIds) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String state = "";
                        ResultSetMetaData rsmd = rs.getMetaData();
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            String upper = rsmd.getColumnName(i).toUpperCase().replace("_", "");
                            if (upper.equals("STATE") || upper.equals("STATUS")) {
                                state = rs.getString(i);
                                break;
                            }
                        }
                        if (!"FINISHED".equalsIgnoreCase(state) && !"APPROVED".equalsIgnoreCase(state)) {
                            throw new BusinessException("Workorder is not finished");
                        }
                    }
                }
            }
        }
    }

    private BigDecimal calculateTotalAmount(Connection c) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        try (PreparedStatement pst = c.prepareStatement("SELECT * FROM TWorkOrders WHERE id = ?")) {
            for (String id : workOrderIds) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            String upper = rsmd.getColumnName(i).toUpperCase().replace("_", "");
                            if (upper.equals("AMOUNT") || upper.equals("TOTALAMOUNT") || upper.equals("TOTAL") || upper.equals("PRICE")) {
                                BigDecimal amount = rs.getBigDecimal(i);
                                if (amount != null) total = total.add(amount);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return total;
    }

    private long generateNextInvoiceNumber(Connection c) {
        try (PreparedStatement pst = c.prepareStatement("SELECT MAX(number) FROM TInvoices");
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) return rs.getLong(1) + 1;
        } catch (SQLException e) { /* Ignored if first run */ }
        return 1L;
    }

    private BigDecimal getVatPercentage(LocalDate date) {
        return date.isBefore(LocalDate.parse("2012-07-01")) ? new BigDecimal("18.0") : new BigDecimal("21.0");
    }

    private void insertInvoiceSafe(Connection c, InvoiceDto dto) throws SQLException {
        try (PreparedStatement pst = c.prepareStatement("SELECT * FROM TInvoices WHERE 1=0");
             ResultSet rs = pst.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            
            StringBuilder sql = new StringBuilder("INSERT INTO TInvoices (");
            StringBuilder values = new StringBuilder(") VALUES (");
            List<Object> params = new ArrayList<>();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String colName = rsmd.getColumnName(i);
                String upper = colName.toUpperCase().replace("_", ""); 
                
                boolean matched = true;
                if (upper.equals("ID")) params.add(dto.id);
                else if (upper.equals("NUMBER")) params.add(dto.number);
                else if (upper.equals("DATE")) params.add(java.sql.Date.valueOf(dto.date));
                
                // Mapeo robusto: captura cualquier nombre de columna para el IVA (porcentaje y cantidad)
                else if (upper.equals("VATPERCENTAGE") || upper.equals("VAT")) params.add(dto.vatPercentage);
                else if (upper.equals("VATAMOUNT")) params.add(dto.vatAmount);
                
                // Mapeo robusto: captura cualquier nombre de columna para el total y el subtotal
                else if (upper.equals("SUBTOTAL")) params.add(dto.subtotal);
                else if (upper.equals("TOTAL") || upper.equals("AMOUNT") || upper.equals("TOTALAMOUNT")) params.add(dto.total);
                
                else if (upper.equals("STATE") || upper.equals("STATUS")) params.add(dto.state);
                else if (upper.equals("VERSION")) params.add(dto.version);
                else matched = false;
                
                if (matched) {
                    sql.append(colName).append(",");
                    values.append("?,");
                }
            }
            
            if (params.isEmpty()) throw new SQLException("No valid columns found in TInvoices.");
            
            sql.setLength(sql.length() - 1);
            values.setLength(values.length() - 1);
            String finalSql = sql.toString() + values.toString() + ")";
            
            try (PreparedStatement insertPst = c.prepareStatement(finalSql)) {
                for (int i = 0; i < params.size(); i++) {
                    insertPst.setObject(i + 1, params.get(i));
                }
                insertPst.executeUpdate();
            }
        }
    }

    private void linkWorkOrders(Connection c, String invoiceId) throws SQLException {
        try (PreparedStatement pst = c.prepareStatement("SELECT * FROM TWorkOrders WHERE 1=0");
             ResultSet rs = pst.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            
            String invoiceIdCol = null;
            String stateCol = null;
            String versionCol = null;
            
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String col = rsmd.getColumnName(i);
                String upper = col.toUpperCase().replace("_", "");
                
                // Permite mapear 'invoice_id', 'invoiceid' y variantes
                if (upper.equals("INVOICEID") || upper.equals("INVOICE")) invoiceIdCol = col;
                else if (upper.equals("STATE") || upper.equals("STATUS")) stateCol = col;
                else if (upper.equals("VERSION")) versionCol = col;
            }
            
            StringBuilder sql = new StringBuilder("UPDATE TWorkOrders SET ");
            if (invoiceIdCol != null) sql.append(invoiceIdCol).append(" = ?, ");
            if (stateCol != null) sql.append(stateCol).append(" = 'INVOICED', ");
            if (versionCol != null) sql.append(versionCol).append(" = ").append(versionCol).append(" + 1, ");
            
            if (sql.toString().endsWith("SET ")) return; 
            sql.setLength(sql.length() - 2);
            sql.append(" WHERE id = ?");
            
            try (PreparedStatement updatePst = c.prepareStatement(sql.toString())) {
                for (String id : workOrderIds) {
                    int paramIdx = 1;
                    if (invoiceIdCol != null) updatePst.setString(paramIdx++, invoiceId);
                    updatePst.setString(paramIdx, id);
                    updatePst.executeUpdate();
                }
            }
        }
    }
}