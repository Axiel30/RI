package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String nif = Console.readString("nif");

        Console.println("\nMechanic information \n");

        Factories.service.forMechanicCrudService().findByNif(nif).ifPresentOrElse(Printer::printMechanic,
            () -> Console.println("There is no mechanic with that nif"));
    }

}
