package uo.ri.cws.application.persistence.recommendation;

import java.util.List;
import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;

public interface RecommendationGateway extends Gateway<RecommendationRecord> {

	List<RecommendationRecord> findNotUsedBySponsorId(String sponsorId);

	void markAsUsed(String id);

	public static class RecommendationRecord extends BaseRecord {
		public String sponsor_id;
		public String recommended_id;
		public boolean usedForVoucher;
	}
}