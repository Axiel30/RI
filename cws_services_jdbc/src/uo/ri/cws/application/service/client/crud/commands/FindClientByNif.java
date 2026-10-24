package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindClientByNif implements Command<Optional<ClientDto>> {

	private final ClientGateway clients = Factories.persistence.forClient();
	private final String nif;

	public FindClientByNif(String nif) {
		ArgumentChecks.isNotBlank(nif, "The nif cannot be null or blank");
		this.nif = nif;
	}

	@Override
	public Optional<ClientDto> execute() {
		return clients.findByNif(nif).map(ClientDtoAssembler::toDto);
	}

}
