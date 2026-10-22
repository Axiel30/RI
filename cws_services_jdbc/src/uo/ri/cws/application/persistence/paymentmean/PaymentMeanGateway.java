package uo.ri.cws.application.persistence.paymentmean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface PaymentMeanGateway extends Gateway<PaymentMeanGateway.PaymentMeanRecord> {

	List<PaymentMeanRecord> findByClientId(String clientId);

	Optional<VoucherRecord> findVoucherByCode(String code);

	Optional<CreditCardRecord> findCreditCardByNumber(String number);

	List<VoucherRecord> findVouchersByClientNif(String nif);

	boolean hasCharges(String paymentMeanId);

	/**
	 * @return one row per client with vouchers, the ones with more available amount
	 *         first
	 */
	List<VoucherSummaryRecord> findVoucherSummaries();

	class PaymentMeanRecord extends Gateway.BaseRecord {
		public String dtype;
		public String clientId;
		public BigDecimal accumulated = BigDecimal.ZERO;
	}

	class CashRecord extends PaymentMeanRecord {
		public CashRecord() {
			dtype = "Cash";
		}
	}

	class VoucherRecord extends PaymentMeanRecord {
		public String code;
		public String description;
		public BigDecimal available;

		public VoucherRecord() {
			dtype = "Voucher";
		}
	}

	class CreditCardRecord extends PaymentMeanRecord {
		public String number;
		public String type;
		public LocalDate validThru;

		public CreditCardRecord() {
			dtype = "CreditCard";
		}
	}

	record VoucherSummaryRecord(String nif, String name, String surname, long issued, BigDecimal total,
			BigDecimal available, BigDecimal consumed) {
	}

}
