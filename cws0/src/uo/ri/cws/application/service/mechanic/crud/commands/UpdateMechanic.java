package uo.ri.cws.application.service.mechanic.crud.commands;

import java.time.LocalDateTime;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {
	private MechanicGateway mg = Factories.persistence.forMechanic();
	private MechanicDto dto;

	public UpdateMechanic(MechanicDto dto) {
		ArgumentChecks.isNotNull(dto, "DTO cannot be null");
		ArgumentChecks.isNotBlank(dto.id, "Id cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.nif, "NIF cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.name, "Name cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.surname, "Surname cannot be null or blank");
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<MechanicRecord> om = mg.findById(dto.id);
		if (om.isEmpty()) {
			throw new BusinessException("The mechanic does not exist");
		}

		MechanicRecord record = om.get();
		if (dto.version != record.version) {
			throw new BusinessException("The mechanic has been modified concurrently");
		}

		record.nif = dto.nif;
		record.name = dto.name;
		record.surname = dto.surname;
		record.updatedAt = LocalDateTime.now();

		mg.update(record);
		return null;
	}
}