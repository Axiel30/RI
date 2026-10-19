package uo.ri.cws.application.persistence.workorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.util.exception.PersistenceException;

public interface WorkOrderGateway extends Gateway<WorkOrderRecord> {

	List<WorkOrderRecord> findNotInvoicedByClientNif(String nif) throws PersistenceException;

	List<WorkOrderRecord> findByIds(List<String> workOrderIds) throws PersistenceException;

	List<WorkOrderRecord> findByClientId(String clientId);

	public static class WorkOrderRecord extends BaseRecord {
		public BigDecimal amount;
		public LocalDateTime date;
		public String description;
		public String state;
		public String invoice_id;
		public String mechanic_id;
		public String vehicle_id;
	}
}