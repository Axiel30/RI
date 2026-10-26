package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CashRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeletePaymentMean implements Command<Void> {

	private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
	private final String id;

	public DeletePaymentMean(String id) {
		ArgumentChecks.isNotBlank(id, "The payment mean id cannot be null or blank");
		this.id = id;
	}

	@Override
	public Void execute() throws BusinessException {
		Optional<PaymentMeanRecord> paymentMean = paymentMeans.findById(id);
		BusinessChecks.exists(paymentMean, "The payment mean does not exist");
		BusinessChecks.isFalse(paymentMean.get() instanceof CashRecord, "Cash cannot be deleted");
		BusinessChecks.isFalse(paymentMeans.hasCharges(id), "The payment mean has charges");

		paymentMeans.remove(id);
		return null;
	}

}
