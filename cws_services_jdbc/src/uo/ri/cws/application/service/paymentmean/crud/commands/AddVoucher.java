package uo.ri.cws.application.service.paymentmean.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddVoucher implements Command<VoucherDto> {

    private final ClientGateway clients = Factories.persistence.forClient();
    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();
    private final VoucherDto voucher;

    public AddVoucher(VoucherDto voucher) {
        ArgumentChecks.isNotNull(voucher, "The voucher cannot be null");
        ArgumentChecks.isNotBlank(voucher.clientId, "The client id cannot be null or blank");
        ArgumentChecks.isNotBlank(voucher.code, "The voucher code cannot be null or blank");
        ArgumentChecks.isNotBlank(voucher.description, "The voucher description cannot be null or blank");
        ArgumentChecks.isNotNull(voucher.availableAmount, "The available amount cannot be null");
        ArgumentChecks.isTrue(voucher.availableAmount.signum() >= 0, "The available amount cannot be negative");
        this.voucher = voucher;
    }

    @Override
    public VoucherDto execute() throws BusinessException {
        BusinessChecks.exists(clients.findById(voucher.clientId), "The client does not exist");
        BusinessChecks.doesNotExist(paymentMeans.findVoucherByCode(voucher.code),
            "There is already a voucher with that code");

        VoucherRecord record = PaymentMeanDtoAssembler.toRecord(voucher);
        paymentMeans.add(record);
        return PaymentMeanDtoAssembler.toDto(record);
    }

}
