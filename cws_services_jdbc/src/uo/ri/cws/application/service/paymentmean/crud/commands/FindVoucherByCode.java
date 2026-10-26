package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.assertion.ArgumentChecks;

public class FindVoucherByCode implements Command<Optional<VoucherDto>> {

    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
    private final String code;

    public FindVoucherByCode(String code) {
        ArgumentChecks.isNotBlank(code, "The voucher code cannot be null or blank");
        this.code = code;
    }

    @Override
    public Optional<VoucherDto> execute() {
        return paymentMeans.findVoucherByCode(code).map(PaymentMeanDtoAssembler::toDto);
    }

}
