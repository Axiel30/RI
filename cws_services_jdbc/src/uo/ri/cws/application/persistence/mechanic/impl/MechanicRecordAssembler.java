package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

class MechanicRecordAssembler extends BaseRecordAssembler<MechanicRecord> {

	@Override
	protected MechanicRecord newRecord(ResultSet rs) {
		return new MechanicRecord();
	}

	@Override
	protected void fill(MechanicRecord record, ResultSet rs) throws SQLException {
		record.nif = rs.getString("nif");
		record.name = rs.getString("name");
		record.surname = rs.getString("surname");
	}

}
