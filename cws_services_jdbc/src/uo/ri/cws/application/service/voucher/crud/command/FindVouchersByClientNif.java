package uo.ri.cws.application.service.voucher.crud.command;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindVouchersByClientNif implements Command<List<VoucherDto>> {

	private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
	private final String nif;

	public FindVouchersByClientNif(String nif) {
		ArgumentChecks.isNotBlank(nif, "The nif cannot be null or blank");
		this.nif = nif;
	}

	@Override
	public List<VoucherDto> execute() {
		return paymentMeans.findVouchersByClientNif(nif).stream().map(FindVouchersByClientNif::toDto).toList();
	}

	private static VoucherDto toDto(VoucherRecord record) {
		VoucherDto dto = new VoucherDto();
		dto.id = record.id;
		dto.version = record.version;
		dto.clientId = record.clientId;
		dto.accumulatedAmount = record.accumulated;
		dto.code = record.code;
		dto.description = record.description;
		dto.availableAmount = record.available;
		return dto;
	}

}
