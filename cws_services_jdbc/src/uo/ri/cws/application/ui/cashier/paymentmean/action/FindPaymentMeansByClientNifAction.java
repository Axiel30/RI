package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CashDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindPaymentMeansByClientNifAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String nif = Console.readString("Client nif");

        PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
        List<PaymentMeanDto> paymentMeans = service.findPaymentMeansByClientNif(nif);

        Console.println("\nClient's payment means:\n");
        for (PaymentMeanDto pm : paymentMeans) {
            printPaymentMean(pm);
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