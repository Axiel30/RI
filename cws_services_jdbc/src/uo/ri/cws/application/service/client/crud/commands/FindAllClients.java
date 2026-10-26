package uo.ri.cws.application.service.client.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

public class FindAllClients implements Command<List<ClientDto>> {

    private final ClientGateway clients = Factories.persistence.forClient();

    @Override
    public List<ClientDto> execute() {
        return ClientDtoAssembler.toDtoList(clients.findAll());
    }

}
