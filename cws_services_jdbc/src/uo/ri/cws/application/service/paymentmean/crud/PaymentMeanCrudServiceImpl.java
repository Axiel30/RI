package uo.ri.cws.application.service.paymentmean.crud;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CashRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class PaymentMeanCrudServiceImpl implements PaymentMeanCrudService {

	private final CommandExecutor executor = new CommandExecutor();
	private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
	private final ClientGateway clients = Factories.persistence.forClient();

	@Override
	public CardDto addCard(CardDto card) throws BusinessException {
		ArgumentChecks.isNotNull(card, "The card cannot be null");
		ArgumentChecks.isNotBlank(card.clientId, "The client id cannot be null or blank");
		ArgumentChecks.isNotBlank(card.cardNumber, "The card number cannot be null or blank");
		ArgumentChecks.isNotBlank(card.cardType, "The card type cannot be null or blank");
		ArgumentChecks.isNotNull(card.cardExpiration, "The card expiration cannot be null");

		return executor.execute(() -> {
			BusinessChecks.exists(clients.findById(card.clientId), "The client does not exist");
			BusinessChecks.doesNotExist(paymentMeans.findCreditCardByNumber(card.cardNumber),
					"There is already a credit card with that number");
			BusinessChecks.isFalse(card.cardExpiration.isBefore(YearMonth.now()), "The credit card is outdated");

			CreditCardRecord record = PaymentMeanDtoAssembler.toRecord(card);
			paymentMeans.add(record);
			return PaymentMeanDtoAssembler.toDto(record);
		});
	}

	@Override
	public VoucherDto addVoucher(VoucherDto voucher) throws BusinessException {
		ArgumentChecks.isNotNull(voucher, "The voucher cannot be null");
		ArgumentChecks.isNotBlank(voucher.clientId, "The client id cannot be null or blank");
		ArgumentChecks.isNotBlank(voucher.code, "The voucher code cannot be null or blank");
		ArgumentChecks.isNotBlank(voucher.description, "The voucher description cannot be null or blank");
		ArgumentChecks.isNotNull(voucher.availableAmount, "The available amount cannot be null");
		ArgumentChecks.isTrue(voucher.availableAmount.signum() >= 0, "The available amount cannot be negative");

		return executor.execute(() -> {
			BusinessChecks.exists(clients.findById(voucher.clientId), "The client does not exist");
			BusinessChecks.doesNotExist(paymentMeans.findVoucherByCode(voucher.code),
					"There is already a voucher with that code");

			VoucherRecord record = PaymentMeanDtoAssembler.toRecord(voucher);
			paymentMeans.add(record);
			return PaymentMeanDtoAssembler.toDto(record);
		});
	}

	@Override
	public void deletePaymentMean(String id) throws BusinessException {
		ArgumentChecks.isNotBlank(id, "The payment mean id cannot be null or blank");

		executor.execute(() -> {
			Optional<PaymentMeanRecord> paymentMean = paymentMeans.findById(id);
			BusinessChecks.exists(paymentMean, "The payment mean does not exist");
			BusinessChecks.isFalse(paymentMean.get() instanceof CashRecord, "Cash cannot be deleted");
			BusinessChecks.isFalse(paymentMeans.hasCharges(id), "The payment mean has charges");

			paymentMeans.remove(id);
			return null;
		});
	}

	@Override
	public Optional<PaymentMeanDto> findById(String id) throws BusinessException {
		ArgumentChecks.isNotBlank(id, "The payment mean id cannot be null or blank");

		return executor.execute(() -> paymentMeans.findById(id).map(PaymentMeanDtoAssembler::toDto));
	}

	@Override
	public List<PaymentMeanDto> findPaymentMeansByClientNif(String nif) throws BusinessException {
		ArgumentChecks.isNotBlank(nif, "The nif cannot be null or blank");

		return executor.execute(() -> {
			Optional<ClientRecord> client = clients.findByNif(nif);
			if (client.isEmpty()) {
				return List.of();
			}
			return paymentMeans.findByClientId(client.get().id).stream().map(PaymentMeanDtoAssembler::toDto).toList();
		});
	}

	@Override
	public Optional<VoucherDto> findVoucherByCode(String code) throws BusinessException {
		ArgumentChecks.isNotBlank(code, "The voucher code cannot be null or blank");

		return executor.execute(() -> paymentMeans.findVoucherByCode(code).map(PaymentMeanDtoAssembler::toDto));
	}

	@Override
	public Optional<CardDto> findCreditCardByNumber(String cardNumber) throws BusinessException {
		ArgumentChecks.isNotBlank(cardNumber, "The card number cannot be null or blank");

		return executor
				.execute(() -> paymentMeans.findCreditCardByNumber(cardNumber).map(PaymentMeanDtoAssembler::toDto));
	}

}
