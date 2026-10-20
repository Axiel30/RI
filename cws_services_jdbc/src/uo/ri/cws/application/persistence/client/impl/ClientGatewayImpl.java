package uo.ri.cws.application.persistence.client.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class ClientGatewayImpl implements ClientGateway {

	@Override
	public void add(ClientRecord t) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_ADD"))) {
			pst.setString(1, t.id);
			pst.setString(2, t.nif);
			pst.setString(3, t.name);
			pst.setString(4, t.surname);
			pst.setString(5, t.email);
			pst.setString(6, t.phone);
			pst.setString(7, t.city);
			pst.setString(8, t.street);
			pst.setLong(9, t.version > 0 ? t.version : 1L);
			
			// Protegemos contra nulos usando la fecha actual si no vienen del test
			Timestamp now = Timestamp.valueOf(LocalDateTime.now());
			pst.setTimestamp(10, t.createdAt != null ? Timestamp.valueOf(t.createdAt) : now);
			pst.setTimestamp(11, t.updatedAt != null ? Timestamp.valueOf(t.updatedAt) : now);
			pst.setString(12, "ENABLED"); // o t.entityState si lo tienes disponible

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
	@Override
	public void remove(String id) throws PersistenceException {
	}

	@Override
	public void update(ClientRecord t) throws PersistenceException {
	}

	@Override
	public Optional<ClientRecord> findById(String id) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDBYID"))) {
			pst.setString(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next() ? Optional.of(fromResultSet(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Optional<ClientRecord> findByNif(String nif) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDBYNIF"))) {
			pst.setString(1, nif);
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next() ? Optional.of(fromResultSet(rs)) : Optional.empty();
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<ClientRecord> findAll() throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		List<ClientRecord> res = new ArrayList<>();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCLIENTS_FINDALL"));
				ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				res.add(fromResultSet(rs));
			}
			return res;
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	private ClientRecord fromResultSet(ResultSet rs) throws SQLException {
		ClientRecord r = new ClientRecord();
		r.id = rs.getString("id");
		r.version = rs.getLong("version");
		r.nif = rs.getString("nif");
		r.name = rs.getString("name");
		r.surname = rs.getString("surname");
		r.email = rs.getString("email");
		r.phone = rs.getString("phone");
		r.city = rs.getString("city");
		r.street = rs.getString("street");
		return r;
	}
}