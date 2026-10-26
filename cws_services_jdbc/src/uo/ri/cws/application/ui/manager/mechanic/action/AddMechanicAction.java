package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {
        MechanicDto mechanic = new MechanicDto();
        mechanic.nif = Console.readString("nif");
        mechanic.name = Console.readString("Name");
        mechanic.surname = Console.readString("Surname");

        Factories.service.forMechanicCrudService().create(mechanic);

        Console.println("Mechanic added");
    }

}
