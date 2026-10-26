package uo.ri.cws.application.service.client.crud.commands;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteClient implements Command<Void> {

    private final ClientGateway clients = Factories.persistence.forClient();
    private final VehicleGateway vehicles = Factories.persistence.forVehicle();
    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
    private final RecommendationGateway recommendations = Factories.persistence.forRecommendation();
    private final String nif;

    public DeleteClient(String nif) {
        ArgumentChecks.isNotBlank(nif, "The nif cannot be null or blank");
        this.nif = nif;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ClientRecord> found = clients.findByNif(nif);
        BusinessChecks.exists(found, "The client does not exist");
        String clientId = found.get().id;

        BusinessChecks.isFalse(vehicles.existsByClientId(clientId), "The client has vehicles");
        List<PaymentMeanRecord> clientPaymentMeans = paymentMeans.findByClientId(clientId);
        for (PaymentMeanRecord paymentMean : clientPaymentMeans) {
            BusinessChecks.isFalse(paymentMeans.hasCharges(paymentMean.id), "The client has paid invoices");
        }

        for (RecommendationRecord recommendation : recommendations.findByClientId(clientId)) {
            recommendations.remove(recommendation.id);
        }
        for (PaymentMeanRecord paymentMean : clientPaymentMeans) {
            paymentMeans.remove(paymentMean.id);
        }
        clients.remove(clientId);
        return null;
    }

}
