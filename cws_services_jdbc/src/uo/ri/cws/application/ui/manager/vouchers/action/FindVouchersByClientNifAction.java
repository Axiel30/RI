package uo.ri.cws.application.ui.manager.vouchers.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindVouchersByClientNifAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String nif = Console.readString("Client nif");

        VoucherGenerationService service = Factories.service.forVoucherService();
        List<VoucherDto> vouchers = service.findVouchersByClientNif(nif);

        Console.println("\nClient's vouchers\n");
        if (vouchers.isEmpty()) {
            Console.println("\tNo vouchers found for this client.");
        } else {
            for (VoucherDto v : vouchers) {
                Printer.printVoucher(v);
            }
        }
    }

}