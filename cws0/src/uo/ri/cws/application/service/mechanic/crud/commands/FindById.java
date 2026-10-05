package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class FindById {
	private static final String TMECHANICS_FINDBYNIF = "SELECT ID, NAME, SURNAME, nif, VERSION FROM TMECHANICS "
			+ "WHERE ID = ?";

	private String id;

	public FindById(String id) {
		this.id = id;
	}

	public MechanicDto execute() throws BusinessException {
		MechanicDto dto = new MechanicDto();

		try (Connection c = Jdbc.createThreadConnection()) {
			try (PreparedStatement pst = c.prepareStatement(TMECHANICS_FINDBYNIF)) {
				pst.setString(1, id);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						dto.id = rs.getString(1);
						dto.name = rs.getString(2);
						dto.surname = rs.getString(3);
						dto.nif = rs.getString(4);
						dto.version = rs.getLong(5);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return dto;
	}
}
