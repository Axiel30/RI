package uo.ri.cws.application.persistence.paymentmean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;

public interface PaymentMeanGateway extends Gateway<PaymentMeanRecord> {

	void addVoucher(VoucherRecord voucher);

	void addCreditCard(CreditCardRecord card);

	void removeVoucher(String id);

	void removeCreditCard(String id);

	Optional<VoucherRecord> findVoucherById(String id);

	Optional<VoucherRecord> findVoucherByCode(String code);

	List<VoucherRecord> findVouchersByClientId(String clientId);

	List<VoucherRecord> findVouchersByClientNif(String nif);

	Optional<CreditCardRecord> findCreditCardById(String id);

	Optional<CreditCardRecord> findCreditCardByNumber(String number);

	List<CreditCardRecord> findCreditCardsByClientId(String clientId);

	Optional<CashRecord> findCashById(String id);

	List<CashRecord> findCashesByClientId(String clientId);

	List<PaymentMeanRecord> findByClientId(String clientId);

	boolean hasCharges(String paymentMeanId);

	public static class PaymentMeanRecord extends BaseRecord {
		public String dtype;
		public double accumulated;
		public String client_id;
	}

	public static class VoucherRecord extends PaymentMeanRecord {
		public String code;
		public double available;
		public String description;
	}

	public static class CreditCardRecord extends PaymentMeanRecord {
		public String number;
		public String type;
		public LocalDate validThru;
	}

	public static class CashRecord extends PaymentMeanRecord {
	}
}