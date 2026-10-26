package uo.ri.cws.application.persistence.intervention.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.intervention.InterventionGateway.InterventionRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

class InterventionRecordAssembler extends BaseRecordAssembler<InterventionRecord> {

    @Override
    protected InterventionRecord newRecord(ResultSet rs) {
        return new InterventionRecord();
    }

    @Override
    protected void fill(InterventionRecord record, ResultSet rs) throws SQLException {
        record.date = rs.getTimestamp("date").toLocalDateTime();
        record.minutes = rs.getInt("minutes");
        record.mechanicId = rs.getString("mechanic_id");
        record.workOrderId = rs.getString("workorder_id");
    }

}
