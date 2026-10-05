package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class UpdateMechanic {
	private static final String TMECHANICS_FINDBYID = "select * from TMechanics where id = ?";
	private static final String TMECHANICS_UPDATE = "update TMechanics set name = ?, surname = ?, "
			+ "version = version + 1, updatedat = ?" + "where id = ?";

	private MechanicDto dto;

	public UpdateMechanic(MechanicDto dto) {
		this.dto = dto;
	}

	public void execute() throws BusinessException {
		// Get info
		String id = dto.id;

		// check mechanic exists
		checkMechanicExists(id);
		// Ask for new data
		// nif is the identity, cannot be changed
		String name = dto.name;
		String surname = dto.surname;

		// update
		updateMechanic(id, name, surname);

		// Print result
		Console.println("Mechanic updated");
	}

	private void updateMechanic(String id, String name, String surname) {
		// Process
		try (Connection c = Jdbc.createThreadConnection()) {
			try (PreparedStatement pst = c.prepareStatement(TMECHANICS_UPDATE)) {
				pst.setString(1, name);
				pst.setString(2, surname);
				pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				pst.setString(4, id);

				pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void checkMechanicExists(String id) throws BusinessException {
		try (Connection c = Jdbc.createThreadConnection()) {
			try (PreparedStatement pst = c.prepareStatement(TMECHANICS_FINDBYID)) {
				pst.setString(1, id);
				try (ResultSet rs = pst.executeQuery()) {
					if (!rs.next()) {
						throw new BusinessException("Mechanic does not exist");
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
