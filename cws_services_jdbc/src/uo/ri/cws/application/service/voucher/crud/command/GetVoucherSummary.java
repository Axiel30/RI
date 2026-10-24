package uo.ri.cws.application.service.voucher.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherSummaryRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;

public class GetVoucherSummary implements Command<List<VoucherSummaryDto>> {

	private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();

	@Override
	public List<VoucherSummaryDto> execute() {
		return paymentMeans.findVoucherSummaries().stream().map(GetVoucherSummary::toDto).toList();
	}

	private static VoucherSummaryDto toDto(VoucherSummaryRecord record) {
		return new VoucherSummaryDto(record.nif(), record.name(), record.surname(), record.issued(), record.total(),
				record.available(), record.consumed());
	}

}
