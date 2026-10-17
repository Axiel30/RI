package uo.ri.cws.application.service.cashier.crud.commands;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.util.exception.BusinessException;

public class SettleInvoice {
    private String invoiceId;
    private Map<String, BigDecimal> charges;

    public SettleInvoice(String invoiceId, Map<String, BigDecimal> charges) {
        this.invoiceId = invoiceId;
        this.charges = charges;
    }

    public void execute() throws BusinessException {
        if (invoiceId == null || invoiceId.trim().isEmpty()) throw new IllegalArgumentException("InvoiceId cannot be null or empty");
        if (charges == null || charges.isEmpty()) throw new IllegalArgumentException("Charges cannot be null or empty");

        for (Map.Entry<String, BigDecimal> entry : charges.entrySet()) {
            if (entry.getKey() == null || entry.getKey().trim().isEmpty()) throw new IllegalArgumentException("Payment mean id cannot be null");
            if (entry.getValue() == null) throw new IllegalArgumentException("Amount cannot be null");
        }

        try (Connection c = Jdbc.createThreadConnection()) {
            c.setAutoCommit(false); // Iniciamos transacción
            try {
                String sqlCheck = "SELECT state, amount FROM TInvoices WHERE id = ?";
                try (PreparedStatement pst = c.prepareStatement(sqlCheck)) {
                    pst.setString(1, invoiceId);
                    try (ResultSet rs = pst.executeQuery()) {
                        if (!rs.next()) {
                            throw new BusinessException("Invoice does not exist");
                        }
                        if ("PAID".equalsIgnoreCase(rs.getString("state"))) {
                            throw new BusinessException("Invoice is already paid");
                        }
                        
                        BigDecimal invoiceTotal = rs.getBigDecimal("amount");
                        BigDecimal totalCharges = charges.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                        
                        if (invoiceTotal.compareTo(totalCharges) != 0) {
                            throw new BusinessException("Charges do not equal invoice amount");
                        }
                    }
                }

                String sqlUpdateInvoice = "UPDATE TInvoices SET state = 'PAID', version = version + 1 WHERE id = ?";
                try (PreparedStatement pst = c.prepareStatement(sqlUpdateInvoice)) {
                    pst.setString(1, invoiceId);
                    pst.executeUpdate();
                }

                // Aquí iría el UPDATE lógico a la tabla TPaymentMeans para acumular el pago.

                c.commit();
            } catch (SQLException | BusinessException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}