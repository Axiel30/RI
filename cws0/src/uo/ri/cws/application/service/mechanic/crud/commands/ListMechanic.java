package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class ListMechanic implements Command<Optional<MechanicDto>> {
	private MechanicGateway mg = Factories.persistence.forMechanic();
	private String nif;

	public ListMechanic(String nif) {
		ArgumentChecks.isNotNull(nif, "NIF cannot be null");
		ArgumentChecks.isNotBlank(nif, "NIF cannot be blank");
		this.nif = nif;
	}

	@Override
	public Optional<MechanicDto> execute() throws BusinessException {
		return MechanicDtoAssembler.toDto(mg.findbyNif(nif));
	}
}