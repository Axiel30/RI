package uo.ri.cws.application.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindVoucherByCodeAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String code = Console.readString("Voucher code");

        PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
        Optional<VoucherDto> voucher = service.findVoucherByCode(code);

        if (voucher.isPresent()) {
            VoucherDto v = voucher.get();
            Console.println("\nVoucher found:");
            Console.printf(
                "\t[VOUCHER] Id: %s \tClient: %s \tCode: %-12.12s \tDesc: %-25.25s \tAvailable: %.2f \tAccumulated: %.2f\n",
                v.id, v.clientId, v.code, v.description, v.availableAmount, v.accumulatedAmount);
        } else {
            Console.println("\nNo voucher found with that code.");
        }
    }

}