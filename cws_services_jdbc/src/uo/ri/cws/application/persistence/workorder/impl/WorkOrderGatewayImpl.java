package uo.ri.cws.application.persistence.workorder.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.exception.PersistenceException;

public class WorkOrderGatewayImpl implements WorkOrderGateway {

	@Override
	public void add(WorkOrderRecord t) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_ADD"))) {
			pst.setString(1, t.id);
			pst.setBigDecimal(2, t.amount);
			pst.setTimestamp(3, Timestamp.valueOf(t.date));
			pst.setString(4, t.description);
			pst.setString(5, t.state);
			pst.setString(6, t.invoice_id);
			pst.setString(7, t.mechanic_id);
			pst.setString(8, t.vehicle_id);
			pst.setLong(9, t.version);
			pst.setTimestamp(10, Timestamp.valueOf(t.createdAt));
			pst.setTimestamp(11, Timestamp.valueOf(t.updatedAt));
			pst.setString(12, t.entityState);

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void remove(String id) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_REMOVE"))) {
			pst.setString(1, id);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void update(WorkOrderRecord t) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_UPDATE"))) {
			pst.setString(1, t.description);
			pst.setBigDecimal(2, t.amount);
			pst.setString(3, t.state);
			pst.setString(4, t.invoice_id);
			pst.setString(5, t.mechanic_id);
			pst.setString(6, t.vehicle_id);
			pst.setTimestamp(7, Timestamp.valueOf(t.updatedAt != null ? t.updatedAt : LocalDateTime.now()));
			pst.setString(8, t.id);

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Optional<WorkOrderRecord> findById(String id) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDBYID"))) {
			pst.setString(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					WorkOrderRecordAssembler assembler = new WorkOrderRecordAssembler(WorkOrderRecord::new);
					return Optional.of(assembler.toRecord(rs));
				}
				return Optional.empty();
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<WorkOrderRecord> findByIds(List<String> workOrderIds) throws PersistenceException {
		List<WorkOrderRecord> result = new ArrayList<>();
		for (String id : workOrderIds) {
			findById(id).ifPresent(result::add);
		}
		return result;
	}

	@Override
	public List<WorkOrderRecord> findAll() throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDALL"))) {
			try (ResultSet rs = pst.executeQuery()) {
				List<WorkOrderRecord> records = new ArrayList<>();
				WorkOrderRecordAssembler assembler = new WorkOrderRecordAssembler(WorkOrderRecord::new);
				while (rs.next()) {
					records.add(assembler.toRecord(rs));
				}
				return records;
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<WorkOrderRecord> findNotInvoicedByClientNif(String nif) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FIND_NOT_INVOICED"))) {
			pst.setString(1, nif);
			try (ResultSet rs = pst.executeQuery()) {
				List<WorkOrderRecord> records = new ArrayList<>();
				WorkOrderRecordAssembler assembler = new WorkOrderRecordAssembler(WorkOrderRecord::new);
				while (rs.next()) {
					records.add(assembler.toRecord(rs));
				}
				return records;
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<WorkOrderRecord> findByClientId(String clientId) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FIND_BY_CLIENT_ID"))) {
			pst.setString(1, clientId);
			try (ResultSet rs = pst.executeQuery()) {
				List<WorkOrderRecord> records = new ArrayList<>();
				WorkOrderRecordAssembler assembler = new WorkOrderRecordAssembler(WorkOrderRecord::new);
				while (rs.next()) {
					records.add(assembler.toRecord(rs));
				}
				return records;
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}