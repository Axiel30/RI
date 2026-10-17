package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class MechanicDtoAssembler {
	public static MechanicRecord toRecord(MechanicDto dto) {
		MechanicRecord record = new MechanicRecord();
		record.nif = dto.nif;
		record.name = dto.name;
		record.surname = dto.surname;
		return record;
	}

	public static MechanicDto toDto(MechanicRecord record) {
		MechanicDto dto = new MechanicDto();
		dto.id = record.id;
		dto.nif = record.nif;
		dto.name = record.name;
		dto.surname = record.surname;
		dto.version = record.version;
		return dto;
	}

	public static Optional<MechanicDto> toDto(Optional<MechanicRecord> arg) {
		return arg.map(MechanicDtoAssembler::toDto);
	}

	public static List<MechanicDto> toDtoList(List<MechanicRecord> list) {
		List<MechanicDto> res = new ArrayList<>();
		for (MechanicRecord r : list) {
			res.add(toDto(r));
		}
		return res;
	}
}
