package uo.ri.cws.application.service.mechanic.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.crud.commands.AddMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.DeleteMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.FindMechanicById;
import uo.ri.cws.application.service.mechanic.crud.commands.FindMechanicByNif;
import uo.ri.cws.application.service.mechanic.crud.commands.ListAllMechanics;
import uo.ri.cws.application.service.mechanic.crud.commands.UpdateMechanic;
import uo.ri.util.exception.BusinessException;

public class MechanicCrudServiceImpl implements MechanicCrudService {

    private final CommandExecutor executor = new CommandExecutor();

    @Override
    public MechanicDto create(MechanicDto mechanic) throws BusinessException {
        return executor.execute(new AddMechanic(mechanic));
    }

    @Override
    public void delete(String idMechanic) throws BusinessException {
        executor.execute(new DeleteMechanic(idMechanic));
    }

    @Override
    public void update(MechanicDto mechanic) throws BusinessException {
        executor.execute(new UpdateMechanic(mechanic));
    }

    @Override
    public Optional<MechanicDto> findById(String id) throws BusinessException {
        return executor.execute(new FindMechanicById(id));
    }

    @Override
    public Optional<MechanicDto> findByNif(String nif) throws BusinessException {
        return executor.execute(new FindMechanicByNif(nif));
    }

    @Override
    public List<MechanicDto> findAll() throws BusinessException {
        return executor.execute(new ListAllMechanics());
    }
}
