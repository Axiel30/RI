package uo.ri.cws.application.service.client.crud.commands;

import java.util.List;

import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.assertion.ArgumentChecks;

public class ClientDtoAssembler {

	/**
	 * Checks that the dto is not null and none of its data is null or blank (the id
	 * is not data)
	 */
	static void checkHasData(ClientDto dto) {
		ArgumentChecks.isNotNull(dto, "The client cannot be null");
		ArgumentChecks.isNotBlank(dto.nif, "The nif cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.name, "The name cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.surname, "The surname cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.addressStreet, "The street cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.addressCity, "The city cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.addressCountry, "The country cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.addressZipcode, "The zip code cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.phone, "The phone cannot be null or blank");
		ArgumentChecks.isNotBlank(dto.email, "The email cannot be null or blank");
	}

	static ClientRecord toRecord(ClientDto dto) {
		ClientRecord record = new ClientRecord();
		record.nif = dto.nif;
		record.name = dto.name;
		record.surname = dto.surname;
		record.street = dto.addressStreet;
		record.city = dto.addressCity;
		record.country = dto.addressCountry;
		record.postalCode = dto.addressZipcode;
		record.phone = dto.phone;
		record.email = dto.email;
		return record;
	}

	static ClientDto toDto(ClientRecord record) {
		ClientDto dto = new ClientDto();
		dto.id = record.id;
		dto.version = record.version;
		dto.nif = record.nif;
		dto.name = record.name;
		dto.surname = record.surname;
		dto.addressStreet = record.street;
		dto.addressCity = record.city;
		dto.addressCountry = record.country;
		dto.addressZipcode = record.postalCode;
		dto.phone = record.phone;
		dto.email = record.email;
		return dto;
	}

	static List<ClientDto> toDtoList(List<ClientRecord> records) {
		return records.stream().map(ClientDtoAssembler::toDto).toList();
	}

}
