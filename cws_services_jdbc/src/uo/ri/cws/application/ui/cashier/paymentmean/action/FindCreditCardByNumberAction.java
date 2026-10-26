package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindCreditCardByNumberAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String cardNumber = Console.readString("Card number");

        PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
        Optional<CardDto> card = service.findCreditCardByNumber(cardNumber);

        if (card.isPresent()) {
            Console.println("\nCredit card found:");
            Printer.printCard(card.get());
        } else {
            Console.println("\nNo credit card found with that number.");
        }
    }

}