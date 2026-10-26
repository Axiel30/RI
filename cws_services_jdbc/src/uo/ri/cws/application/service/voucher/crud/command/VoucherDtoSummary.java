package uo.ri.cws.application.service.voucher.crud.command;

import java.util.List;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;

class VoucherDtoAssembler {

    static VoucherDto toDto(VoucherRecord record) {
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

    static List<VoucherDto> toDtoList(List<VoucherRecord> records) {
        return records.stream().map(VoucherDtoAssembler::toDto).toList();
    }

}
