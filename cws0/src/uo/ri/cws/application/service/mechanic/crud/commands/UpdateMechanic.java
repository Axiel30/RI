package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class UpdateMechanic {
	private static final String TMECHANICS_UPDATE = "update TMechanics set nif = ?, name = ?, surname = ?, "
			+ "version = version + 1, updatedat = ? where id = ? and version = ?";

	private MechanicDto dto;

	public UpdateMechanic(MechanicDto dto) {
		this.dto = dto;
	}

	public void execute() throws BusinessException {
		if (dto == null)
			throw new IllegalArgumentException("DTO cannot be null");
		if (dto.id == null || dto.id.trim().isEmpty())
			throw new IllegalArgumentException("Id cannot be null or blank");
		if (dto.nif == null || dto.nif.trim().isEmpty())
			throw new IllegalArgumentException("NIF cannot be null or blank");
		if (dto.name == null || dto.name.trim().isEmpty())
			throw new IllegalArgumentException("Name cannot be null or blank");
		if (dto.surname == null || dto.surname.trim().isEmpty())
			throw new IllegalArgumentException("Surname cannot be null or blank");

		try (Connection c = Jdbc.createThreadConnection()) {
			try (PreparedStatement pst = c.prepareStatement(TMECHANICS_UPDATE)) {
				pst.setString(1, dto.nif);
				pst.setString(2, dto.name);
				pst.setString(3, dto.surname);
				pst.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				pst.setString(5, dto.id);
				// Se pasa la versión del DTO como sexto parámetro
				pst.setLong(6, dto.version);

				int rowsAffected = pst.executeUpdate();
				if (rowsAffected == 0) {
					// Si no actualiza ninguna fila, o no existe o la versión cambió (fue modificado
					// por otro usuario)
					throw new BusinessException("The mechanic does not exist or has been modified concurrently");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}