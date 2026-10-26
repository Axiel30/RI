package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.time.YearMonth;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddCard implements Command<CardDto> {

    private final ClientGateway clients = Factories.persistence.forClient();
    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
    private final CardDto card;

    public AddCard(CardDto card) {
        ArgumentChecks.isNotNull(card, "The card cannot be null");
        ArgumentChecks.isNotBlank(card.clientId, "The client id cannot be null or blank");
        ArgumentChecks.isNotBlank(card.cardNumber, "The card number cannot be null or blank");
        ArgumentChecks.isNotBlank(card.cardType, "The card type cannot be null or blank");
        ArgumentChecks.isNotNull(card.cardExpiration, "The card expiration cannot be null");
        this.card = card;
    }

    @Override
    public CardDto execute() throws BusinessException {
        BusinessChecks.exists(clients.findById(card.clientId), "The client does not exist");
        BusinessChecks.doesNotExist(paymentMeans.findCreditCardByNumber(card.cardNumber),
            "There is already a credit card with that number");
        BusinessChecks.isFalse(card.cardExpiration.isBefore(YearMonth.now()), "The credit card is outdated");

        CreditCardRecord record = PaymentMeanDtoAssembler.toRecord(card);
        paymentMeans.add(record);
        return PaymentMeanDtoAssembler.toDto(record);
    }

}
