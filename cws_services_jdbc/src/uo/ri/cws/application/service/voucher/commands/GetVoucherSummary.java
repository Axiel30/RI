package uo.ri.cws.application.service.voucher.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.util.exception.BusinessException;

public class GetVoucherSummary implements Command<List<VoucherSummaryDto>> {

    private ClientGateway cg = Factories.persistence.forClient();
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();

    @Override
    public List<VoucherSummaryDto> execute() throws BusinessException {
        List<VoucherSummaryDto> result = new ArrayList<>();
        List<ClientRecord> clients = cg.findAll();

        for (ClientRecord c : clients) {
            List<VoucherRecord> vouchers = pmg.findVouchersByClientId(c.id);
            if (vouchers.isEmpty()) {
                continue;
            }
            long issued = vouchers.size();
            double available = vouchers.stream().mapToDouble(v -> v.available).sum();
            double consumed = vouchers.stream().mapToDouble(v -> v.accumulated).sum();
            double total = available + consumed;

            VoucherSummaryDto summary = new VoucherSummaryDto(
                    c.nif,
                    c.name,
                    c.surname,
                    issued,
                    BigDecimal.valueOf(total),
                    BigDecimal.valueOf(available),
                    BigDecimal.valueOf(consumed)
            );
            result.add(summary);
        }
        result.sort(Comparator.comparing(VoucherSummaryDto::availableAmount).reversed());
        return result;
    }
}