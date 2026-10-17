package uo.ri.cws.application.persistence.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.util.exception.PersistenceException;

public interface InvoiceGateway extends Gateway<InvoiceRecord> {

	Optional<InvoiceRecord> findByNumber(Long number) throws PersistenceException;

	Long getNextInvoiceNumber() throws PersistenceException;

	public static class InvoiceRecord extends BaseRecord {
	    public BigDecimal subtotal;
	    public BigDecimal vatRate;
	    public BigDecimal vatAmount;
	    public BigDecimal total;
	    
	    public long number;
	    public LocalDate date;
	    public String state;
	}
}