package uo.ri.cws.application.service.cashier.crud.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoice implements Command<InvoiceDto> {

	private static final BigDecimal VAT_PERCENTAGE = new BigDecimal("21.0");
	private static final String APPROVED = "APPROVED";
	private static final String INVOICED = "INVOICED";
	private static final String ISSUED = "ISSUED";

	private final InvoiceGateway invoices = Factories.persistence.forInvoice();
	private final WorkOrderGateway workOrders = Factories.persistence.forWorkOrder();
	private final List<String> workOrderIds;

	public CreateInvoice(List<String> workOrderIds) {
		ArgumentChecks.isNotNull(workOrderIds, "The work order ids cannot be null");
		ArgumentChecks.isFalse(workOrderIds.isEmpty(), "The work order ids cannot be empty");
		workOrderIds.forEach(id -> ArgumentChecks.isNotBlank(id, "A work order id cannot be null or blank"));
		this.workOrderIds = workOrderIds.stream().distinct().toList();
	}

	@Override
	public InvoiceDto execute() throws BusinessException {
		List<WorkOrderRecord> toInvoice = loadApprovedWorkOrders();

		InvoiceRecord invoice = new InvoiceRecord();
		invoice.number = invoices.getNextInvoiceNumber();
		invoice.date = LocalDate.now();
		invoice.state = ISSUED;
		invoice.vatRate = VAT_PERCENTAGE;
		invoice.subtotal = toInvoice.stream().map(wo -> wo.amount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2,
				RoundingMode.HALF_EVEN);
		invoice.vatAmount = invoice.subtotal.multiply(VAT_PERCENTAGE).divide(BigDecimal.valueOf(100), 2,
				RoundingMode.HALF_EVEN);
		invoice.total = invoice.subtotal.add(invoice.vatAmount);
		invoices.add(invoice);

		for (WorkOrderRecord workOrder : toInvoice) {
			workOrder.state = INVOICED;
			workOrder.invoiceId = invoice.id;
			workOrder.updatedAt = LocalDateTime.now();
			workOrders.update(workOrder);
		}
		return InvoicingDtoAssembler.toDto(invoice);
	}

	private List<WorkOrderRecord> loadApprovedWorkOrders() throws BusinessException {
		List<WorkOrderRecord> found = new ArrayList<>();
		for (String id : workOrderIds) {
			Optional<WorkOrderRecord> workOrder = workOrders.findById(id);
			BusinessChecks.exists(workOrder, "The work order " + id + " does not exist");
			BusinessChecks.isTrue(APPROVED.equals(workOrder.get().state),
					"The work order " + id + " is not " + APPROVED);
			found.add(workOrder.get());
		}
		return found;
	}

}
