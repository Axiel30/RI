package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListAllMechanicsAction implements Action {

    @Override
    public void execute() throws BusinessException {
        Console.println("\nList of mechanics \n");

        Factories.service.forMechanicCrudService().findAll().forEach(Printer::printMechanic);
    }

}
