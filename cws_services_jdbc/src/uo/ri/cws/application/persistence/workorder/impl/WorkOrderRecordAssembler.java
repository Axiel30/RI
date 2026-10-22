package uo.ri.cws.application.persistence.workorder.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;

class WorkOrderRecordAssembler extends BaseRecordAssembler<WorkOrderRecord> {

	@Override
	protected WorkOrderRecord newRecord(ResultSet rs) {
		return new WorkOrderRecord();
	}

	@Override
	protected void fill(WorkOrderRecord record, ResultSet rs) throws SQLException {
		record.description = rs.getString("description");
		record.date = rs.getTimestamp("date").toLocalDateTime();
		record.amount = rs.getBigDecimal("total_amount");
		record.state = rs.getString("state");
		record.invoiceId = rs.getString("invoice_id");
		record.mechanicId = rs.getString("mechanic_id");
		record.vehicleId = rs.getString("vehicle_id");
	}

}
