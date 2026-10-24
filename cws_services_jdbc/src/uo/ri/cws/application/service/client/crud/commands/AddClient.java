package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CashRecord;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddClient implements Command<ClientDto> {

	private final ClientGateway clients = Factories.persistence.forClient();
	private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
	private final RecommendationGateway recommendations = Factories.persistence.forRecommendation();
	private final ClientDto dto;
	private final String recommenderNif;

	public AddClient(ClientDto dto, String recommenderNif) {
		ClientDtoAssembler.checkHasData(dto);
		if (recommenderNif != null) {
			ArgumentChecks.isNotBlank(recommenderNif, "The recommender nif cannot be blank");
		}
		this.dto = dto;
		this.recommenderNif = recommenderNif;
	}

	@Override
	public ClientDto execute() throws BusinessException {
		BusinessChecks.doesNotExist(clients.findByNif(dto.nif), "There is already a client with that nif");
		Optional<ClientRecord> recommender = findRecommender();

		ClientRecord client = ClientDtoAssembler.toRecord(dto);
		clients.add(client);

		CashRecord cash = new CashRecord();
		cash.clientId = client.id;
		paymentMeans.add(cash);

		if (recommender.isPresent()) {
			RecommendationRecord recommendation = new RecommendationRecord();
			recommendation.sponsorId = recommender.get().id;
			recommendation.recommendedId = client.id;
			recommendations.add(recommendation);
		}

		dto.id = client.id;
		dto.version = client.version;
		return dto;
	}

	private Optional<ClientRecord> findRecommender() throws BusinessException {
		if (recommenderNif == null) {
			return Optional.empty();
		}
		Optional<ClientRecord> recommender = clients.findByNif(recommenderNif);
		BusinessChecks.exists(recommender, "The recommender client does not exist");
		return recommender;
	}

}
