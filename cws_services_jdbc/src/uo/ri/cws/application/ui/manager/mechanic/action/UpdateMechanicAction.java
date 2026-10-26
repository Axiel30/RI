package uo.ri.cws.application.ui.manager.mechanic.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String id = Console.readString("Type mechanic id to update");

        MechanicCrudService service = Factories.service.forMechanicCrudService();
        Optional<MechanicDto> found = service.findById(id);
        if (found.isEmpty()) {
            Console.println("Mechanic does not exist");
            return;
        }

        MechanicDto mechanic = found.get();
        mechanic.name = Console.readString("Name");
        mechanic.surname = Console.readString("Surname");

        service.update(mechanic);

        Console.println("Mechanic updated");
    }

}
