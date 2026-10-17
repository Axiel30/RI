package uo.ri.cws.application.service.cashier.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrders {
	private String nif;

	private static final String TWORKORDERS_FINDNOTINVOICED = "select a.id, a.description, a.date, a.state, a.total_amount"
			+ " from TWorkOrders as a, TVehicles as v, TClients as c" + " where a.vehicle_id = v.id"
			+ " and v.client_id = c.id and state like 'APPROVED' and nif like ?";

	public FindNotInvoicedWorkOrders(String nif) {
		this.nif = nif;
	}

	public List<InvoicingWorkOrderDto> execute() throws BusinessException {
		if (nif == null || nif.trim().isEmpty()) {
			throw new IllegalArgumentException("NIF cannot be null or empty");
		}

		List<InvoicingWorkOrderDto> result = new ArrayList<>();

		try (Connection c = Jdbc.createThreadConnection()) {
			try (PreparedStatement pst = c.prepareStatement(TWORKORDERS_FINDNOTINVOICED)) {
				pst.setString(1, nif);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
						dto.id = rs.getString(1);
						dto.description = rs.getString(2);
						dto.date = rs.getTimestamp(3) != null ? rs.getTimestamp(3).toLocalDateTime() : null;
						dto.state = rs.getString(4);
						// rs.getDouble(5) convertido a BigDecimal
						dto.amount = java.math.BigDecimal.valueOf(rs.getDouble(5));
						result.add(dto);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return result;
	}
}