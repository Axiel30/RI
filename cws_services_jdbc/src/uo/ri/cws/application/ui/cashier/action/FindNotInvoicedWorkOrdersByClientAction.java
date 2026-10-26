package uo.ri.cws.application.ui.cashier.action;

import uo.ri.conf.Factories;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindNotInvoicedWorkOrdersByClientAction implements Action {

    /**
     * Process: - Ask customer nif - Display all uninvoiced work orders. For each
     * work order, display id, date, status, amount and description
     */
    @Override
    public void execute() throws BusinessException {
        String nif = Console.readString("Client nif ");

        Console.println("\nClient not invoiced work orders\n");

        Factories.service.forCreateInvoiceService().findNotInvoicedWorkOrdersByClientNif(nif)
            .forEach(workOrder -> Console.printf("\t%s \t%-40.40s \t%s \t%-12.12s \t%.2f\n", workOrder.id,
                workOrder.description, workOrder.date, workOrder.state, workOrder.amount));
    }

}
