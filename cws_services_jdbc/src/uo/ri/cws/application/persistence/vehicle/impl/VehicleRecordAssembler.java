package uo.ri.cws.application.persistence.vehicle.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway.VehicleRecord;

class VehicleRecordAssembler extends BaseRecordAssembler<VehicleRecord> {

	@Override
	protected VehicleRecord newRecord(ResultSet rs) {
		return new VehicleRecord();
	}

	@Override
	protected void fill(VehicleRecord record, ResultSet rs) throws SQLException {
		record.licensePlate = rs.getString("licensePlate");
		record.make = rs.getString("make");
		record.model = rs.getString("model");
		record.clientId = rs.getString("client_id");
		record.vehicleTypeId = rs.getString("vehicleType_id");
	}

}
