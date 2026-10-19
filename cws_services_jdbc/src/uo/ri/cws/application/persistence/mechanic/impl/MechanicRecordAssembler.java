package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class MechanicRecordAssembler extends BaseRecordAssembler<MechanicRecord> {

	public MechanicRecordAssembler(Supplier<MechanicRecord> supplier) {
		super(MechanicRecord::new);
	}

	@Override
	public MechanicRecord toRecord(ResultSet rs) throws SQLException {
		MechanicRecord r = super.toRecord(rs);
		r.nif = rs.getString("nif");
		r.name = rs.getString("name");
		r.surname = rs.getString("surname");
		return r;
	}
}
