package uo.ri.cws.application.service.client.crud.commands;

import java.time.LocalDateTime;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateClient implements Command<Void> {

	private final ClientGateway clients = Factories.persistence.forClient();
	private final ClientDto dto;

	public UpdateClient(ClientDto dto) {
		ClientDtoAssembler.checkHasData(dto);
		ArgumentChecks.isNotBlank(dto.id, "The id cannot be null or blank");
		this.dto = dto;
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<ClientRecord> found = clients.findById(dto.id);
		BusinessChecks.exists(found, "The client does not exist");
		BusinessChecks.hasVersion(found.get().version, dto.version);

		Optional<ClientRecord> sameNif = clients.findByNif(dto.nif);
		BusinessChecks.isTrue(sameNif.isEmpty() || sameNif.get().id.equals(dto.id),
				"There is already a client with that nif");

		ClientRecord record = ClientDtoAssembler.toRecord(dto);
		record.id = dto.id;
		record.updatedAt = LocalDateTime.now();
		clients.update(record);
		return null;
	}

}
