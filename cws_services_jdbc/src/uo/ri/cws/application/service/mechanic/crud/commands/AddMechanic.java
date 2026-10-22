package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {

	private final MechanicGateway mechanics = Factories.persistence.forMechanic();
	private final MechanicDto dto;

	public AddMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto, "The mechanic cannot be null");
		ArgumentChecks.isNotBlank(dto.nif, "The nif cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.name, "The name cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.surname, "The surname cannot be null or blank");
		this.dto = dto;
	}

	@Override
	public MechanicDto execute() throws BusinessException {
		BusinessChecks.doesNotExist(mechanics.findByNif(dto.nif), "There is already a mechanic with that nif");

		MechanicRecord record = MechanicDtoAssembler.toRecord(dto);
		mechanics.add(record);

		dto.id = record.id;
		dto.version = record.version;
		return dto;
	}

}
