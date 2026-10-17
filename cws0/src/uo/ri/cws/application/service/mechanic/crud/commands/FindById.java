package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindById implements Command<Optional<MechanicDto>> {
	private MechanicGateway mg = Factories.persistence.forMechanic();
	private String id;

	public FindById(String id) {
		ArgumentChecks.isNotNull(id, "Id cannot be null");
		ArgumentChecks.isNotBlank(id, "Id cannot be blank");
		this.id = id;
	}

	@Override
	public Optional<MechanicDto> execute() throws BusinessException {
		return MechanicDtoAssembler.toDto(mg.findById(id));
	}
}