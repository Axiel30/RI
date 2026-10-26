package uo.ri.cws.application.ui.cashier.action;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

/**
 * Settling an invoice is not available yet: the service layer does not
 * implement InvoicingService.settleInvoice.
 */
public class SettleInvoiceAction implements Action {

    @Override
    public void execute() throws BusinessException {
        Console.println("Settle invoice is not available yet");
    }

}
