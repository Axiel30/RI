package uo.ri.cws.application.service.voucher.commands;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindVouchersByClientNif implements Command<List<VoucherDto>> {

    private String nif;
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();

    public FindVouchersByClientNif(String nif) {
        ArgumentChecks.isNotBlank(nif);
        this.nif = nif;
    }

    @Override
    public List<VoucherDto> execute() throws BusinessException {
        return pmg.findVouchersByClientNif(nif).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private VoucherDto toDto(VoucherRecord r) {
        VoucherDto dto = new VoucherDto();
        dto.id = r.id;
        dto.version = r.version;
        dto.clientId = r.client_id;
        dto.accumulatedAmount = BigDecimal.valueOf(r.accumulated);
        dto.code = r.code;
        dto.description = r.description;
        dto.availableAmount = BigDecimal.valueOf(r.available);
//        dto.available = r.available;
        return dto;
    }
}