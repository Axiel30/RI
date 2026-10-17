package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.PersistenceException;

public class DeleteMechanic implements Command<Void> {
	private MechanicGateway mg = Factories.persistence.forMechanic();
	private String idMechanic;

	public DeleteMechanic(String idMechanic) {
		ArgumentChecks.isNotNull(idMechanic, "The id cannot be null");
		ArgumentChecks.isNotBlank(idMechanic, "The id cannot be blank");
		this.idMechanic = idMechanic;
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<MechanicRecord> om = mg.findById(idMechanic);
		if (om.isEmpty()) {
			throw new BusinessException("The mechanic does not exist");
		}

		try {
			mg.remove(idMechanic);
		} catch (PersistenceException e) {
			throw new BusinessException("The mechanic cannot be deleted (has related records)");
		}
		return null;
	}
}