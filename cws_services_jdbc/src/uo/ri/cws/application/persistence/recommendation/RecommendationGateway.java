package uo.ri.cws.application.persistence.recommendation;

import java.util.List;

import uo.ri.cws.application.persistence.Gateway;

public interface RecommendationGateway extends Gateway<RecommendationGateway.RecommendationRecord> {

	/*
	 * @return the recommendations made or received by the client
	 */
	List<RecommendationRecord> findByClientId(String clientId);

	/**
	 * @return the recommendations made by the sponsor not yet used to get a voucher
	 */
	List<RecommendationRecord> findNotUsedBySponsorId(String sponsorId);

	class RecommendationRecord extends Gateway.BaseRecord {
		public String sponsorId;
		public String recommendedId;
		public boolean usedForVoucher;
	}

}
