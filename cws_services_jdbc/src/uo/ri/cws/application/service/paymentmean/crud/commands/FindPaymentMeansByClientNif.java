package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindPaymentMeansByClientNif implements Command<List<PaymentMeanDto>> {

    private final ClientGateway clients = Factories.persistence.forClient();
    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
    private final String nif;

    public FindPaymentMeansByClientNif(String nif) {
        ArgumentChecks.isNotBlank(nif, "The nif cannot be null or blank");
        this.nif = nif;
    }

    @Override
    public List<PaymentMeanDto> execute() {
        Optional<ClientRecord> client = clients.findByNif(nif);
        if (client.isEmpty()) {
            return List.of();
        }
        return paymentMeans.findByClientId(client.get().id).stream().map(PaymentMeanDtoAssembler::toDto).toList();
    }

}
