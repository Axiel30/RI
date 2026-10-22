package uo.ri.cws.application.persistence.intervention;

import java.time.LocalDateTime;

import uo.ri.cws.application.persistence.Gateway;

public interface InterventionGateway extends Gateway<InterventionGateway.InterventionRecord> {

	boolean existsByMechanicId(String mechanicId);

	class InterventionRecord extends Gateway.BaseRecord {
		public LocalDateTime date;
		public int minutes;
		public String mechanicId;
		public String workOrderId;
	}

}
