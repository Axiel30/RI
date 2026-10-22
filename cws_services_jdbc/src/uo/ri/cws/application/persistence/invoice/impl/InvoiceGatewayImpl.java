package uo.ri.cws.application.persistence.invoice.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class InvoiceGatewayImpl implements InvoiceGateway {

	private final InvoiceRecordAssembler assembler = new InvoiceRecordAssembler();

	@Override
	public void add(InvoiceRecord invoice) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TINVOICES_ADD"))) {
			pst.setString(1, invoice.id);
			pst.setLong(2, invoice.number);
			pst.setDate(3, Date.valueOf(invoice.date));
			pst.setBigDecimal(4, invoice.vatRate);
			pst.setBigDecimal(5, invoice.vatAmount);
			pst.setBigDecimal(6, invoice.subtotal);
			pst.setBigDecimal(7, invoice.total);
			pst.setString(8, invoice.state);
			pst.setLong(9, invoice.version);
			pst.setTimestamp(10, Timestamp.valueOf(invoice.createdAt));
			pst.setTimestamp(11, Timestamp.valueOf(invoice.updatedAt));
			pst.setString(12, invoice.entityState);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void remove(String id) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TINVOICES_REMOVE"))) {
			pst.setString(1, id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void update(InvoiceRecord invoice) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TINVOICES_UPDATE"))) {
			pst.setDate(1, Date.valueOf(invoice.date));
			pst.setBigDecimal(2, invoice.vatRate);
			pst.setBigDecimal(3, invoice.vatAmount);
			pst.setBigDecimal(4, invoice.subtotal);
			pst.setBigDecimal(5, invoice.total);
			pst.setString(6, invoice.state);
			pst.setTimestamp(7, Timestamp.valueOf(invoice.updatedAt));
			pst.setString(8, invoice.id);
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public Optional<InvoiceRecord> findById(String id) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TINVOICES_FINDBYID"))) {
			pst.setString(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				return assembler.toOptionalRecord(rs);
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<InvoiceRecord> findAll() {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TINVOICES_FINDALL")); ResultSet rs = pst.executeQuery()) {
			return assembler.toRecordList(rs);

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public long getNextInvoiceNumber() {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TINVOICES_LASTNUMBER")); ResultSet rs = pst.executeQuery()) {
			rs.next();
			return rs.getLong(1) + 1; // MAX() of an empty table is NULL, read as 0
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public boolean hasPaidInvoices(String clientId) {
		try (PreparedStatement pst = Jdbc.getCurrentConnection()
				.prepareStatement(Queries.getSQLSentence("TINVOICES_EXISTSPAIDBYCLIENTID"))) {
			pst.setString(1, clientId);
			try (ResultSet rs = pst.executeQuery()) {
				return rs.next();
			}

		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

}
