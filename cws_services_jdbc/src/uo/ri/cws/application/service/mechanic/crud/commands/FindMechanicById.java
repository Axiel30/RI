package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindMechanicById implements Command<Optional<MechanicDto>> {

    private final MechanicGateway mechanics = Factories.persistence.forMechanic();
    private final String id;

    public FindMechanicById(String id) {
        ArgumentChecks.isNotBlank(id, "The id cannot be null or blank");
        this.id = id;
    }

    @Override
    public Optional<MechanicDto> execute() {
        return mechanics.findById(id).map(MechanicDtoAssembler::toDto);
    }

}
