package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {
	private MechanicGateway mg = Factories.persistence.forMechanic();
	private MechanicDto dto;
	
	public AddMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto,"DTO cannot be null");
		ArgumentChecks.isNotBlank(dto.nif,"NIF cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.name,"Name cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.surname,"Surname cannot be null or blank");
		this.dto = dto;
	}
	
	@Override
	public MechanicDto execute() throws BusinessException {
		MechanicRecord record =  MechanicDtoAssembler.toRecord(dto);
		mg.add(record);
		dto.id = record.id;
		dto.version = record.version;
		return dto;
	}
}