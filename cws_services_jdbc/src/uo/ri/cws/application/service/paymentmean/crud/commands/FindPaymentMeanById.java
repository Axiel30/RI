package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindPaymentMeanById implements Command<Optional<PaymentMeanDto>> {

    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
    private final String id;

    public FindPaymentMeanById(String id) {
        ArgumentChecks.isNotBlank(id, "The payment mean id cannot be null or blank");
        this.id = id;
    }

    @Override
    public Optional<PaymentMeanDto> execute() {
        return paymentMeans.findById(id).map(PaymentMeanDtoAssembler::toDto);
    }

}
