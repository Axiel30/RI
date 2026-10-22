package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class ListAllMechanics implements Command<List<MechanicDto>> {

	private final MechanicGateway mechanics = Factories.persistence.forMechanic();

	@Override
	public List<MechanicDto> execute() {
		return MechanicDtoAssembler.toDtoList(mechanics.findAll());
	}

}
