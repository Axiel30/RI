package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class AddMechanic {
	private static final String TMECHANICS_ADD = "insert into TMechanics" + "(id, nif, name, surname, version, "
			+ "createdAt, updatedAt, entityState) " + "values (?, ?, ?, ?, ?, ?, ?, ?)";

	private MechanicDto dto;

	public AddMechanic(MechanicDto dto) {
		this.dto = dto;
	}

	public MechanicDto execute() throws BusinessException {
		if(new ListAllMechanics().execute().contains(dto)) {
			throw new BusinessException("Mechanic is repetead");
		}
		
		String nif = dto.nif;
		String name = dto.name;
		String surname = dto.surname;
		String id = dto.id = UUID.randomUUID().toString();
		long version = dto.version = 1;
		
		// Process
		try (Connection c = Jdbc.createThreadConnection();) {
			try (PreparedStatement pst = c.prepareStatement(TMECHANICS_ADD)) {
				pst.setString(1, id);
				pst.setString(2, nif);
				pst.setString(3, name);
				pst.setString(4, surname);
				pst.setLong(5, version);
				pst.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
				pst.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
				pst.setString(8, "ENABLED");

				pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		// Print result
		Console.println("Mechanic added");
		return this.dto;
	}
}
