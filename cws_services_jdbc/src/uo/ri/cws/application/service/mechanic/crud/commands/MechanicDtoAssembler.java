package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.List;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

class MechanicDtoAssembler {

    static MechanicRecord toRecord(MechanicDto dto) {
        MechanicRecord record = new MechanicRecord();
        record.nif = dto.nif;
        record.name = dto.name;
        record.surname = dto.surname;
        return record;
    }

    static MechanicDto toDto(MechanicRecord record) {
        MechanicDto dto = new MechanicDto();
        dto.id = record.id;
        dto.version = record.version;
        dto.nif = record.nif;
        dto.name = record.name;
        dto.surname = record.surname;
        return dto;
    }

    static List<MechanicDto> toDtoList(List<MechanicRecord> records) {
        return records.stream().map(MechanicDtoAssembler::toDto).toList();
    }

}
