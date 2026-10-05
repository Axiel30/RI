package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class DeleteMechanic {
	private static final String TMECHANICS_DELETE = "DELETE FROM TMECHANICS WHERE ID = ?";
	private String idMechanic;

	public DeleteMechanic(String idMechanic) {
		this.idMechanic = idMechanic;
	}

	public void execute() throws BusinessException {
		if (idMechanic == null || idMechanic.trim().isEmpty()) {
			throw new IllegalArgumentException("The id cannot be null or blank");
		}

		try (Connection c = Jdbc.createThreadConnection()) {
			try (PreparedStatement pst = c.prepareStatement(TMECHANICS_DELETE)) {
				pst.setString(1, idMechanic);

				int rowsAffected = pst.executeUpdate();
				if (rowsAffected == 0) {
					throw new BusinessException("The mechanic does not exist");
				}
			}
		} catch (SQLException e) {
			// Si la BBDD tira error de integridad referencial, el mecánico tiene relaciones
			throw new BusinessException("The mechanic cannot be deleted (has related records)");
		}
	}
}