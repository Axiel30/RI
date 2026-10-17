package uo.ri.cws.application.persistence.workorder.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.function.Supplier;

import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;

public class WorkOrderRecordAssembler extends BaseRecordAssembler<WorkOrderRecord> {

	public WorkOrderRecordAssembler() {
		super(WorkOrderRecord::new);
	}

	public WorkOrderRecordAssembler(Supplier<WorkOrderRecord> supplier) {
		super(WorkOrderRecord::new);
	}

	@Override
	public WorkOrderRecord toRecord(ResultSet rs) throws SQLException {
		WorkOrderRecord r = super.toRecord(rs);
		r.amount = rs.getBigDecimal("total_amount");
		Timestamp date = rs.getTimestamp("date");
		r.date = (date != null) ? date.toLocalDateTime() : null;
		r.description = rs.getString("description");
		r.state = rs.getString("state");
		r.invoice_id = rs.getString("invoice_id");
		r.mechanic_id = rs.getString("mechanic_id");
		r.vehicle_id = rs.getString("vehicle_id");
		return r;
	}
}