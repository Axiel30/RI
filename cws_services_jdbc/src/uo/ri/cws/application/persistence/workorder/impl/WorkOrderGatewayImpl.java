package uo.ri.cws.application.persistence.workorder.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.exception.PersistenceException;

public class WorkOrderGatewayImpl implements WorkOrderGateway {

	private final WorkOrderRecordAssembler assembler = new WorkOrderRecordAssembler();

	@Override
	public void add(WorkOrderRecord workOrder) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TWORKORDERS_ADD"))) {
			pst.setString(1, workOrder.id);
			pst.setTimestamp(2, Timestamp.valueOf(workOrder.date));
			pst.setString(3, workOrder.description);
			pst.setBigDecimal(4, workOrder.amount);
			pst.setString(5, workOrder.state);
			pst.setLong(6, workOrder.version);
			pst.setTimestamp(7, Timestamp.valueOf(workOrder.createdAt));
			pst.setTimestamp(8, Timestamp.valueOf(workOrder.updatedAt));
			pst.setString(9, workOrder.entityState);
			pst.setString(10, workOrder.invoiceId);
			pst.setString(11, workOrder.mechanicId);
			pst.setString(12, workOrder.vehicleId);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void remove(String id) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TWORKORDERS_REMOVE"))) {
			pst.setString(1, id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void update(WorkOrderRecord workOrder) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TWORKORDERS_UPDATE"))) {
			pst.setString(1, workOrder.description);
			pst.setBigDecimal(2, workOrder.amount);
			pst.setString(3, workOrder.state);
			pst.setString(4, workOrder.invoiceId);
			pst.setString(5, workOrder.mechanicId);
			pst.setString(6, workOrder.vehicleId);
			pst.setTimestamp(7, Timestamp.valueOf(workOrder.updatedAt));
			pst.setString(8, workOrder.id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Optional<WorkOrderRecord> findById(String id) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDBYID"))) {
			pst.setString(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				return assembler.toOptionalRecord(rs);
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<WorkOrderRecord> findAll() {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDALL")); ResultSet rs = pst.executeQuery()) {
			return assembler.toRecordList(rs);

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<WorkOrderRecord> findNotInvoicedByClientNif(String nif) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDNOTINVOICEDBYCLIENTNIF"))) {
			pst.setString(1, nif);
			try (ResultSet rs = pst.executeQuery()) {
				return assembler.toRecordList(rs);
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public boolean existsByMechanicId(String mechanicId) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TWORKORDERS_EXISTSBYMECHANICID"))) {
			pst.setString(1, mechanicId);
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next();
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

}
