package uo.ri.cws.application.service.mechanic.crud.commands;

import java.time.LocalDateTime;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {

	private final MechanicGateway mechanics = Factories.persistence.forMechanic();
	private final MechanicDto dto;

	public UpdateMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto, "The mechanic cannot be null");
		ArgumentChecks.isNotBlank(dto.id, "The id cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.nif, "The nif cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.name, "The name cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.surname, "The surname cannot be null or blank");
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<MechanicRecord> found = mechanics.findById(dto.id);
		BusinessChecks.exists(found, "The mechanic does not exist");
		BusinessChecks.hasVersion(found.get().version, dto.version);

		Optional<MechanicRecord> sameNif = mechanics.findByNif(dto.nif);
		BusinessChecks.isTrue(sameNif.isEmpty() || sameNif.get().id.equals(dto.id),
				"There is already a mechanic with that nif");

		MechanicRecord record = MechanicDtoAssembler.toRecord(dto);
		record.id = dto.id;
		record.updatedAt = LocalDateTime.now();
		mechanics.update(record);
		return null;
	}

}
