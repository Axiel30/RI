package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class MechanicGatewayImpl implements MechanicGateway {

	private final MechanicRecordAssembler assembler = new MechanicRecordAssembler();

	@Override
	public void add(MechanicRecord mechanic) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TMECHANICS_ADD"))) {
			pst.setString(1, mechanic.id);
			pst.setString(2, mechanic.nif);
			pst.setString(3, mechanic.name);
			pst.setString(4, mechanic.surname);
			pst.setLong(5, mechanic.version);
			pst.setTimestamp(6, Timestamp.valueOf(mechanic.createdAt));
			pst.setTimestamp(7, Timestamp.valueOf(mechanic.updatedAt));
			pst.setString(8, mechanic.entityState);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void remove(String id) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TMECHANICS_REMOVE"))) {
			pst.setString(1, id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void update(MechanicRecord mechanic) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TMECHANICS_UPDATE"))) {
			pst.setString(1, mechanic.name);
			pst.setString(2, mechanic.surname);
			pst.setString(3, mechanic.nif);
			pst.setTimestamp(4, Timestamp.valueOf(mechanic.updatedAt));
			pst.setString(5, mechanic.id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Optional<MechanicRecord> findById(String id) {
		return findOne("TMECHANICS_FINDBYID", id);
	}

	@Override
	public Optional<MechanicRecord> findByNif(String nif) {
		return findOne("TMECHANICS_FINDBYNIF", nif);
	}

	@Override
	public List<MechanicRecord> findAll() {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TMECHANICS_FINDALL")); ResultSet rs = pst.executeQuery()) {
			return assembler.toRecordList(rs);

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	private Optional<MechanicRecord> findOne(String query, String arg) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection().prepareStatement(Queries.getSQLSentence(query))) {
			pst.setString(1, arg);
			try (ResultSet rs = pst.executeQuery()) {
				return assembler.toOptionalRecord(rs);
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

}
