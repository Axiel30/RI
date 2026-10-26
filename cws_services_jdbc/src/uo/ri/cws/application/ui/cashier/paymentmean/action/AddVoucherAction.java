package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.math.BigDecimal;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddVoucherAction implements Action {

    @Override
    public void execute() throws BusinessException {
        VoucherDto voucher = new VoucherDto();
        voucher.clientId = Console.readString("Client id");
        voucher.code = Console.readString("Voucher code");
        voucher.description = Console.readString("Voucher description");
        double amount = Console.readDouble("Available amount");
        voucher.availableAmount = BigDecimal.valueOf(amount);

        PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
        VoucherDto added = service.addVoucher(voucher);

        Console.println("\nVoucher added:");
        Console.printf(
            "\t[VOUCHER] Id: %s \tClient: %s \tCode: %-12.12s \tDesc: %-25.25s \tAvailable: %.2f \tAccumulated: %.2f\n",
            added.id, added.clientId, added.code, added.description, added.availableAmount, added.accumulatedAmount);
    }

}