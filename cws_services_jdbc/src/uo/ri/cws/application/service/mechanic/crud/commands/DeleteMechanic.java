package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {

    private final MechanicGateway mechanics = Factories.persistence.forMechanic();
    private final WorkOrderGateway workOrders = Factories.persistence.forWorkOrder();
    private final InterventionGateway interventions = Factories.persistence.forIntervention();
    private final String mechanicId;

    public DeleteMechanic(String mechanicId) {
        ArgumentChecks.isNotBlank(mechanicId, "The mechanic id cannot be null or blank");
        this.mechanicId = mechanicId;
    }

    @Override
    public Void execute() throws BusinessException {
        BusinessChecks.exists(mechanics.findById(mechanicId), "The mechanic does not exist");
        BusinessChecks.isFalse(workOrders.existsByMechanicId(mechanicId), "The mechanic has work orders");
        BusinessChecks.isFalse(interventions.existsByMechanicId(mechanicId), "The mechanic has interventions");

        mechanics.remove(mechanicId);
        return null;
    }

}
