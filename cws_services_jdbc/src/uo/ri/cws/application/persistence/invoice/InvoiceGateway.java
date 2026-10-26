package uo.ri.cws.application.persistence.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

import uo.ri.cws.application.persistence.Gateway;

public interface InvoiceGateway extends Gateway<InvoiceGateway.InvoiceRecord> {

    /** @return the highest invoice number, 0 if there are no invoices */
    long findLastNumber();

    /**
     * @return true if some work order of the client was invoiced in an invoice in
     *         the given state
     */
    boolean existsByClientIdAndState(String clientId, String state);

    class InvoiceRecord extends Gateway.BaseRecord {
        public long number;
        public LocalDate date;
        public BigDecimal vatRate;
        public BigDecimal vatAmount;
        public BigDecimal subtotal;
        public BigDecimal total;
        public String state;
        public boolean usedForVoucher;
    }

}
