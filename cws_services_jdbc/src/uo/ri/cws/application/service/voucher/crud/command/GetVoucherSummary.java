package uo.ri.cws.application.service.voucher.crud.command;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;

/**
 * One summary per client with vouchers, the ones with more available amount
 * first. The total amount of a voucher is what is still available plus what was
 * consumed.
 */
public class GetVoucherSummary implements Command<List<VoucherSummaryDto>> {

    private final ClientGateway clients = Factories.persistence.forClient();
    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();

    @Override
    public List<VoucherSummaryDto> execute() {
        Map<String, List<VoucherRecord>> vouchersByClient = paymentMeans.findAllVouchers().stream()
            .collect(Collectors.groupingBy(voucher -> voucher.clientId));

        return vouchersByClient.entrySet().stream()
            .sorted(Comparator.comparing((Map.Entry<String, List<VoucherRecord>> entry) -> available(entry.getValue()))
                .reversed())
            .map(entry -> summarize(clients.findById(entry.getKey()), entry.getValue())).flatMap(Optional::stream)
            .toList();
    }

    private Optional<VoucherSummaryDto> summarize(Optional<ClientRecord> client, List<VoucherRecord> vouchers) {
        return client.map(c -> {
            BigDecimal available = available(vouchers);
            BigDecimal consumed = sum(vouchers, voucher -> voucher.accumulated);
            return new VoucherSummaryDto(c.nif, c.name, c.surname, vouchers.size(), available.add(consumed), available,
                consumed);
        });
    }

    private static BigDecimal available(List<VoucherRecord> vouchers) {
        return sum(vouchers, voucher -> voucher.available);
    }

    private static BigDecimal sum(List<VoucherRecord> vouchers, Function<VoucherRecord, BigDecimal> amount) {
        return vouchers.stream().map(amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
