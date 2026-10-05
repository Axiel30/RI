package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class AddMechanic {
	private static final String TMECHANICS_ADD = "insert into TMechanics" + "(id, nif, name, surname, version, "
			+ "createdAt, updatedAt, entityState) " + "values (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String TMECHANICS_CHECK_NIF = "SELECT id FROM TMechanics WHERE nif = ?";

	private MechanicDto dto;

	public AddMechanic(MechanicDto dto) {
		this.dto = dto;
	}

	public MechanicDto execute() throws BusinessException {
		if (dto == null)
			throw new IllegalArgumentException("DTO cannot be null");
		if (dto.nif == null || dto.nif.trim().isEmpty())
			throw new IllegalArgumentException("NIF cannot be null or blank");
		if (dto.name == null || dto.name.trim().isEmpty())
			throw new IllegalArgumentException("Name cannot be null or blank");
		if (dto.surname == null || dto.surname.trim().isEmpty())
			throw new IllegalArgumentException("Surname cannot be null or blank");

		checkNifNotRepeated(dto.nif);

		dto.id = UUID.randomUUID().toString();
		dto.version = 1;

		try (Connection c = Jdbc.createThreadConnection()) {
			try (PreparedStatement pst = c.prepareStatement(TMECHANICS_ADD)) {
				pst.setString(1, dto.id);
				pst.setString(2, dto.nif);
				pst.setString(3, dto.name);
				pst.setString(4, dto.surname);
				pst.setLong(5, dto.version);
				pst.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
				pst.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
				pst.setString(8, "ENABLED");

				pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return dto;
	}

	private void checkNifNotRepeated(String nif) throws BusinessException {
		try (Connection c = Jdbc.createThreadConnection()) {
			try (PreparedStatement pst = c.prepareStatement(TMECHANICS_CHECK_NIF)) {
				pst.setString(1, nif);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						throw new BusinessException("There already exists another mechanic with the same nif");
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}