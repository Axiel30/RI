package uo.ri.cws.application.persistence.invoice.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class InvoiceRecordAssembler extends BaseRecordAssembler<InvoiceRecord> {

	public InvoiceRecordAssembler() {
		super(InvoiceRecord::new);
	}

	public InvoiceRecordAssembler(Supplier<InvoiceRecord> supplier) {
		super(supplier);
	}

	@Override
	public InvoiceRecord toRecord(ResultSet rs) throws SQLException {
		InvoiceRecord record = super.toRecord(rs); 
		
		record.number = rs.getLong("number");
		
		Date sqlDate = rs.getDate("date");
		if (sqlDate != null) {
			record.date = sqlDate.toLocalDate();
		}
		
		record.vatRate = rs.getBigDecimal("vatrate");
		record.vatAmount = rs.getBigDecimal("vatamount");
		record.subtotal = rs.getBigDecimal("subtotal");
		record.total = rs.getBigDecimal("total");
		
		record.state = rs.getString("state");
		
		return record;
	}
}