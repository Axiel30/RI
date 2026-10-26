package uo.ri.cws.application.persistence.invoice.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

class InvoiceRecordAssembler extends BaseRecordAssembler<InvoiceRecord> {

    @Override
    protected InvoiceRecord newRecord(ResultSet rs) {
        return new InvoiceRecord();
    }

    @Override
    protected void fill(InvoiceRecord record, ResultSet rs) throws SQLException {
        record.number = rs.getLong("number");
        record.date = rs.getDate("date").toLocalDate();
        record.vatRate = rs.getBigDecimal("vatRate");
        record.vatAmount = rs.getBigDecimal("vatAmount");
        record.subtotal = rs.getBigDecimal("subtotal");
        record.total = rs.getBigDecimal("total");
        record.state = rs.getString("state");
        record.usedForVoucher = rs.getBoolean("usedForVoucher");
    }

}
