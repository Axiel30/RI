package uo.ri.cws.application.ui.manager.vouchers.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GenerateVouchersAction implements Action {

    @Override
    public void execute() throws BusinessException {
        VoucherGenerationService service = Factories.service.forVoucherService();

        int generated = service.generateVouchers().length;

        Console.printf("Number of generated vouchers: %d\n", generated);
    }

}