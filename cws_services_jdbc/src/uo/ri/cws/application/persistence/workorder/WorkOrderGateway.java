package uo.ri.cws.application.persistence.workorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;

public interface WorkOrderGateway extends Gateway<WorkOrderGateway.WorkOrderRecord> {

    /** @return the work orders in the given state of the vehicles of the client */
    List<WorkOrderRecord> findByClientNifAndState(String nif, String state);

    boolean existsByMechanicId(String mechanicId);

    class WorkOrderRecord extends Gateway.BaseRecord {
        public String description;
        public LocalDateTime date;
        public BigDecimal amount;
        public String state;
        public String invoiceId;
        public String mechanicId;
        public String vehicleId;
        public boolean usedForVoucher;
    }

}
