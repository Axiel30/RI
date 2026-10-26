package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindCreditCardByNumber implements Command<Optional<CardDto>> {

    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
    private final String cardNumber;

    public FindCreditCardByNumber(String cardNumber) {
        ArgumentChecks.isNotBlank(cardNumber, "The card number cannot be null or blank");
        this.cardNumber = cardNumber;
    }

    @Override
    public Optional<CardDto> execute() {
        return paymentMeans.findCreditCardByNumber(cardNumber).map(PaymentMeanDtoAssembler::toDto);
    }

}
