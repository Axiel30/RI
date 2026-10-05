package uo.ri.cws.application.service.mechanic.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.crud.commands.AddMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.DeleteMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.FindById;
import uo.ri.cws.application.service.mechanic.crud.commands.ListAllMechanics;
import uo.ri.cws.application.service.mechanic.crud.commands.ListMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.UpdateMechanic;
import uo.ri.util.exception.BusinessException;

public class MechanicCrudServiceImpl implements MechanicCrudService {

	@Override
	public MechanicDto create(MechanicDto dto) throws BusinessException {
		return new AddMechanic(dto).execute();
	}

	@Override
	public void delete(String mechanicId) throws BusinessException {
		new DeleteMechanic(mechanicId).execute();
	}

	@Override
	public void update(MechanicDto dto) throws BusinessException {
		new UpdateMechanic(dto).execute();
	}

	@Override
	public Optional<MechanicDto> findById(String id) throws BusinessException {
		Optional<MechanicDto> dto = new FindById(id).execute();
		return dto;
	}

	@Override
	public Optional<MechanicDto> findByNif(String nif) throws BusinessException {
		Optional<MechanicDto> dto = new ListMechanic(nif).execute();
		return dto;
	}

	@Override
	public List<MechanicDto> findAll() throws BusinessException {
		return new ListAllMechanics().execute();
	}

}
