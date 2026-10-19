package uo.ri.cws.application.persistence.invoice.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class InvoiceGatewayImpl implements InvoiceGateway {

	@Override
	public void add(InvoiceRecord t) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINVOICES_INSERT"))) {
			pst.setString(1, t.id);
			pst.setLong(2, t.number);
			pst.setDate(3, Date.valueOf(t.date));
			pst.setBigDecimal(4, t.vatRate);
			pst.setBigDecimal(5, t.vatAmount);
			pst.setBigDecimal(6, t.subtotal);
			pst.setBigDecimal(7, t.total);
			pst.setString(8, t.state);
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
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINVOICES_REMOVE"))) {
			pst.setString(1, id);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void update(InvoiceRecord t) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINVOICES_UPDATE"))) {
			pst.setBigDecimal(1, t.total);
			pst.setBigDecimal(2, t.vatRate);
			pst.setBigDecimal(3, t.vatAmount);
			pst.setBigDecimal(4, t.subtotal);
			pst.setDate(5, Date.valueOf(t.date));
			pst.setString(6, t.state);
			pst.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
			pst.setString(8, t.id);

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Optional<InvoiceRecord> findById(String id) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINVOICES_FINDBYID"))) {
			pst.setString(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					InvoiceRecordAssembler assembler = new InvoiceRecordAssembler(InvoiceRecord::new);
					return Optional.of(assembler.toRecord(rs));
				}
				return Optional.empty();
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<InvoiceRecord> findAll() throws PersistenceException {
		return List.of();
	}

	@Override
	public Optional<InvoiceRecord> findByNumber(Long number) throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINVOICES_FINDBYNUMBER"))) {
			pst.setLong(1, number);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					InvoiceRecordAssembler assembler = new InvoiceRecordAssembler(InvoiceRecord::new);
					return Optional.of(assembler.toRecord(rs));
				}
				return Optional.empty();
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Long getNextInvoiceNumber() throws PersistenceException {
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TINVOICES_SELECT_LAST_NUMBER"))) {
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return rs.getLong(1) + 1;
				}
				return 1L;
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}