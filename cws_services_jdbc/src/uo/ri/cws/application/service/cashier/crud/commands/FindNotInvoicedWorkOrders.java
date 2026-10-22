package uo.ri.cws.application.service.cashier.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindNotInvoicedWorkOrders implements Command<List<InvoicingWorkOrderDto>> {

	private final WorkOrderGateway workOrders = Factories.persistence.forWorkOrder();
	private final String nif;

	public FindNotInvoicedWorkOrders(String nif) {
		ArgumentChecks.isNotBlank(nif, "The nif cannot be null or blank");
		this.nif = nif;
	}

	@Override
	public List<InvoicingWorkOrderDto> execute() {
		return InvoicingDtoAssembler.toWorkOrderDtoList(workOrders.findNotInvoicedByClientNif(nif));
	}

}
