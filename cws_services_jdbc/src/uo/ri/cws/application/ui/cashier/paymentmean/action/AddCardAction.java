package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.time.YearMonth;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddCardAction implements Action {

    @Override
    public void execute() throws BusinessException {
        CardDto card = new CardDto();
        card.clientId = Console.readString("Client id");
        card.cardNumber = Console.readString("Card number");
        card.cardType = Console.readString("Card type");
        int year = Console.readInt("Expiration year (YYYY)");
        int month = Console.readInt("Expiration month (1-12)");
        card.cardExpiration = YearMonth.of(year, month);

        PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
        CardDto added = service.addCard(card);

        Console.println("\nCredit card added:");
        Console.printf(
            "\t[CARD]    Id: %s \tClient: %s \tNumber: %-19.19s \tType: %-10.10s \tExp: %s \tAccumulated: %.2f\n",
            added.id, added.clientId, added.cardNumber, added.cardType, added.cardExpiration, added.accumulatedAmount);
    }

}