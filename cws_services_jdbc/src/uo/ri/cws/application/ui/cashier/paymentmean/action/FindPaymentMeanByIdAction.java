package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CashDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindPaymentMeanByIdAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String id = Console.readString("Payment mean id");

        PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
        Optional<PaymentMeanDto> pm = service.findById(id);

        if (pm.isPresent()) {
            Console.println("\nPayment mean found:");
            printPaymentMean(pm.get());
        } else {
            Console.println("\nNo payment mean found with that id.");
        }
    }

    private void printPaymentMean(PaymentMeanDto pm) {
        if (pm instanceof CardDto c) {
            Console.printf(
                "\t[CARD]    Id: %s \tClient: %s \tNumber: %-19.19s \tType: %-10.10s \tExp: %s \tAccumulated: %.2f\n",
                c.id, c.clientId, c.cardNumber, c.cardType, c.cardExpiration, c.accumulatedAmount);
        } else if (pm instanceof VoucherDto v) {
            Console.printf(
                "\t[VOUCHER] Id: %s \tClient: %s \tCode: %-12.12s \tDesc: %-25.25s \tAvailable: %.2f \tAccumulated: %.2f\n",
                v.id, v.clientId, v.code, v.description, v.availableAmount, v.accumulatedAmount);
        } else if (pm instanceof CashDto cash) {
            Console.printf("\t[CASH]    Id: %s \tClient: %s \tAccumulated: %.2f\n", cash.id, cash.clientId,
                cash.accumulatedAmount);
        }
    }

}