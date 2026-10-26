package uo.ri.cws.application.persistence.client.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

class ClientRecordAssembler extends BaseRecordAssembler<ClientRecord> {

    @Override
    protected ClientRecord newRecord(ResultSet rs) {
        return new ClientRecord();
    }

    @Override
    protected void fill(ClientRecord record, ResultSet rs) throws SQLException {
        record.nif = rs.getString("nif");
        record.name = rs.getString("name");
        record.surname = rs.getString("surname");
        record.email = rs.getString("email");
        record.phone = rs.getString("phone");
        record.street = rs.getString("street");
        record.city = rs.getString("city");
        record.postalCode = rs.getString("postalCode");
        record.country = rs.getString("country");
    }

}
