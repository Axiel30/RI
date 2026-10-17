package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.PersistenceException;

public class MechanicGatewayImpl implements MechanicGateway {

	@Override
	public void add(MechanicRecord t) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();

		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TMECHANICS_ADD"))) {
			pst.setString(1, t.id);
			pst.setString(2, t.nif);
			pst.setString(3, t.name);
			pst.setString(4, t.surname);
			pst.setLong(5, t.version);
			pst.setTimestamp(6, Timestamp.valueOf(t.createdAt));
			pst.setTimestamp(7, Timestamp.valueOf(t.updatedAt));
			pst.setString(8, t.entityState);

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void remove(String id) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MechanicRecord t) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<MechanicRecord> findById(String id) throws PersistenceException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<MechanicRecord> findAll() throws PersistenceException {
		ArrayList<MechanicDto> dtos = new ArrayList<>();
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement("TMECHANICS_FINDALL")) {
			try (ResultSet rs = pst.executeQuery();) {
				while (rs.next()) {
					MechanicDto dto = new MechanicDto();
					dto.id = rs.getString(1);
					dto.name = rs.getString(2);
					dto.surname = rs.getString(3);
					dto.nif = rs.getString(4);
					dto.version = rs.getLong(5);
					dtos.add(dto);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public Optional<MechanicRecord> findbyNif(String nif) throws PersistenceException {

		return Optional.empty();
	}

}
