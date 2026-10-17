package uo.ri.cws.application.service.cashier.crud.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class SettleInvoice implements Command<Void> {
	private InvoiceGateway ig = Factories.persistence.forInvoice();
	private String invoiceId;
	private Map<String, BigDecimal> charges;

	public SettleInvoice(String invoiceId, Map<String, BigDecimal> charges) {
		ArgumentChecks.isNotNull(invoiceId, "InvoiceId cannot be null");
		ArgumentChecks.isNotBlank(invoiceId, "InvoiceId cannot be blank");
		ArgumentChecks.isNotNull(charges, "Charges cannot be null");
		ArgumentChecks.isFalse(charges.isEmpty(), "Charges cannot be empty");

		for (Map.Entry<String, BigDecimal> entry : charges.entrySet()) {
			ArgumentChecks.isNotBlank(entry.getKey(), "Payment mean id cannot be null or blank");
			ArgumentChecks.isNotNull(entry.getValue(), "Amount cannot be null");
		}
		this.invoiceId = invoiceId;
		this.charges = charges;
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<InvoiceRecord> oi = ig.findById(invoiceId);
		if (oi.isEmpty()) {
			throw new BusinessException("Invoice does not exist");
		}

		InvoiceRecord invoice = oi.get();
		if ("PAID".equalsIgnoreCase(invoice.state)) {
			throw new BusinessException("Invoice is already paid");
		}

		BigDecimal totalCharges = charges.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		if (invoice.vatAmount.compareTo(totalCharges) != 0) {
			throw new BusinessException("Charges do not equal invoice amount");
		}

		invoice.state = "PAID";
		invoice.updatedAt = LocalDateTime.now();
		ig.update(invoice);

		return null;
	}
}