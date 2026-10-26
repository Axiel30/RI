package uo.ri.cws.application.persistence.recommendation.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

class RecommendationRecordAssembler extends BaseRecordAssembler<RecommendationRecord> {

    @Override
    protected RecommendationRecord newRecord(ResultSet rs) {
        return new RecommendationRecord();
    }

    @Override
    protected void fill(RecommendationRecord record, ResultSet rs) throws SQLException {
        record.sponsorId = rs.getString("recommender_id");
        record.recommendedId = rs.getString("recommended_id");
        record.usedForVoucher = rs.getBoolean("usedForVoucher");
    }

}
