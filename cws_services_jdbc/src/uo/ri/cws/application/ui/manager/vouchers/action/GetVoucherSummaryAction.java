package uo.ri.cws.application.ui.manager.vouchers.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GetVoucherSummaryAction implements Action {

    @Override
    public void execute() throws BusinessException {
        VoucherGenerationService service = Factories.service.forVoucherService();
        List<VoucherSummaryDto> summaries = service.getVoucherSummary();

        Console.println("\nVoucher summary by client\n");
        if (summaries.isEmpty()) {
            Console.println("\tNo clients with vouchers.");
        } else {
            for (VoucherSummaryDto s : summaries) {
                Printer.printVoucherSummary(s);
            }
        }
    }

}