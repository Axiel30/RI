package uo.ri.cws.application.service.paymentmean.crud;

import java.time.YearMonth;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CashDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;

class PaymentMeanDtoAssembler {

	static CreditCardRecord toRecord(CardDto dto) {
		CreditCardRecord record = new CreditCardRecord();
		record.clientId = dto.clientId;
		record.number = dto.cardNumber;
		record.type = dto.cardType;
		record.validThru = dto.cardExpiration.atEndOfMonth();
		return record;
	}

	static VoucherRecord toRecord(VoucherDto dto) {
		VoucherRecord record = new VoucherRecord();
		record.clientId = dto.clientId;
		record.code = dto.code;
		record.description = dto.description;
		record.available = dto.availableAmount;
		return record;
	}

	static PaymentMeanDto toDto(PaymentMeanRecord record) {
		PaymentMeanDto dto;
		if (record instanceof VoucherRecord voucher) {
			dto = toDto(voucher);
		} else if (record instanceof CreditCardRecord card) {
			dto = toDto(card);
		} else {
			dto = new CashDto();
			fillCommonFields(dto, record);
		}
		return dto;
	}

	static CardDto toDto(CreditCardRecord record) {
		CardDto dto = new CardDto();
		fillCommonFields(dto, record);
		dto.cardNumber = record.number;
		dto.cardType = record.type;
		dto.cardExpiration = YearMonth.from(record.validThru);
		return dto;
	}

	static VoucherDto toDto(VoucherRecord record) {
		VoucherDto dto = new VoucherDto();
		fillCommonFields(dto, record);
		dto.code = record.code;
		dto.description = record.description;
		dto.availableAmount = record.available;
		return dto;
	}

	private static void fillCommonFields(PaymentMeanDto dto, PaymentMeanRecord record) {
		dto.id = record.id;
		dto.version = record.version;
		dto.clientId = record.clientId;
		dto.accumulatedAmount = record.accumulated;
	}

}
