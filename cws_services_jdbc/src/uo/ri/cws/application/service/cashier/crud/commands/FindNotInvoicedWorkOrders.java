package uo.ri.cws.application.service.cashier.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrders implements Command<List<InvoicingWorkOrderDto>> {
	private WorkOrderGateway wg = Factories.persistence.forWorkOrder();
	private String nif;

	public FindNotInvoicedWorkOrders(String nif) {
		ArgumentChecks.isNotNull(nif, "NIF cannot be null");
		ArgumentChecks.isNotBlank(nif, "NIF cannot be blank");
		this.nif = nif;
	}

	@Override
	public List<InvoicingWorkOrderDto> execute() throws BusinessException {
		return InvoicingDtoAssembler.toWorkOrderDtoList(wg.findNotInvoicedByClientNif(nif));
	}
}