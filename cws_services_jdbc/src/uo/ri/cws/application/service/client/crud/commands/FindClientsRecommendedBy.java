package uo.ri.cws.application.service.client.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindClientsRecommendedBy implements Command<List<ClientDto>> {

    private final ClientGateway clients = Factories.persistence.forClient();
    private final String recommenderNif;

    public FindClientsRecommendedBy(String recommenderNif) {
        ArgumentChecks.isNotBlank(recommenderNif, "The recommender nif cannot be null or blank");
        this.recommenderNif = recommenderNif;
    }

    @Override
    public List<ClientDto> execute() {
        return ClientDtoAssembler.toDtoList(clients.findRecommendedByNif(recommenderNif));
    }

}
