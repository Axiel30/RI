package uo.ri.cws.application.service.cashier.crud.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoice implements Command<InvoiceDto> {
	private InvoiceGateway ig = Factories.persistence.forInvoice();
	private WorkOrderGateway wg = Factories.persistence.forWorkOrder();
	private List<String> workOrderIds;

	public CreateInvoice(List<String> workOrderIds) {
		ArgumentChecks.isNotNull(workOrderIds, "WorkOrderIds list cannot be null");
		ArgumentChecks.isFalse(workOrderIds.isEmpty(), "WorkOrderIds list cannot be empty");
		for (String id : workOrderIds) {
			ArgumentChecks.isNotNull(id, "WorkOrder id cannot be null");
			ArgumentChecks.isNotBlank(id, "WorkOrder id cannot be blank");
		}
		this.workOrderIds = workOrderIds;
	}

	@Override
	public InvoiceDto execute() throws BusinessException {
		List<WorkOrderRecord> workOrders = new ArrayList<>();

		for (String id : workOrderIds) {
			Optional<WorkOrderRecord> owo = wg.findById(id);
			if (owo.isEmpty()) {
				throw new BusinessException("Workorder does not exist");
			}
			WorkOrderRecord wo = owo.get();
			if (!"FINISHED".equalsIgnoreCase(wo.state) && !"APPROVED".equalsIgnoreCase(wo.state)) {
				throw new BusinessException("Workorder is not finished");
			}
			workOrders.add(wo);
		}

		BigDecimal subtotal = BigDecimal.ZERO;
		for (WorkOrderRecord wo : workOrders) {
			if (wo.amount != null) {
				subtotal = subtotal.add(wo.amount);
			}
		}

		long number = ig.getNextInvoiceNumber();
		LocalDate date = LocalDate.now();
		BigDecimal vatPercentage = getVatPercentage(date);
		BigDecimal vatAmount = subtotal.multiply(vatPercentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
		BigDecimal total = subtotal.add(vatAmount).setScale(2, RoundingMode.HALF_UP);

		InvoiceDto invoice = new InvoiceDto();
		invoice.id = UUID.randomUUID().toString();
		invoice.number = number;
		invoice.date = date;
		invoice.subtotal = subtotal;
		invoice.vatPercentage = vatPercentage;
		invoice.vatAmount = vatAmount;
		invoice.total = total;
		invoice.state = "ISSUED";
		invoice.version = 1L;

		InvoiceRecord invoiceRecord = InvoicingDtoAssembler.toRecord(invoice);
		ig.add(invoiceRecord);

		for (WorkOrderRecord wo : workOrders) {
			wo.invoice_id = invoiceRecord.id;
			wo.state = "INVOICED";
			wo.updatedAt = LocalDateTime.now();
			wg.update(wo);
		}

		return invoice;
	}

	private BigDecimal getVatPercentage(LocalDate date) {
		return date.isBefore(LocalDate.parse("2012-07-01")) ? new BigDecimal("18.0") : new BigDecimal("21.0");
	}
}