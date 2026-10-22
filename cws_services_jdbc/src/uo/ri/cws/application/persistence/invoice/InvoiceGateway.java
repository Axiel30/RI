package uo.ri.cws.application.persistence.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

import uo.ri.cws.application.persistence.Gateway;

public interface InvoiceGateway extends Gateway<InvoiceGateway.InvoiceRecord> {

	/** @return the number following the highest invoice number, 1 if none */
	long getNextInvoiceNumber();

	/**
	 * @return true if some work order of the client was invoiced in an invoice
	 *         already paid
	 */
	boolean hasPaidInvoices(String clientId);

	class InvoiceRecord extends Gateway.BaseRecord {
		public long number;
		public LocalDate date;
		public BigDecimal vatRate;
		public BigDecimal vatAmount;
		public BigDecimal subtotal;
		public BigDecimal total;
		public String state;
	}

}
