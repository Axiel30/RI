package uo.ri.cws.application.service.voucher.crud.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.command.Command;

/**
 * A client who has paid some invoice gets a voucher of 25 euros for every three
 * of its recommendations not yet used for a voucher whose recommended clients
 * have also paid some invoice. The recommendations rewarded are marked as used.
 * Only the vouchers for recommendations are generated, so the amounts of
 * vouchers for work orders and for big invoices are always zero.
 */
public class GenerateVouchersByRecommendations implements Command<int[]> {

    private static final int RECOMMENDATIONS_PER_VOUCHER = 3;
    private static final BigDecimal VOUCHER_AMOUNT = new BigDecimal("25.00");
    private static final String VOUCHER_DESCRIPTION = "Por recomendación";
    private static final String PAID = "PAID";

    private final ClientGateway clients = Factories.persistence.forClient();
    private final InvoiceGateway invoices = Factories.persistence.forInvoice();
    private final RecommendationGateway recommendations = Factories.persistence.forRecommendation();
    private final PaymentMeanGateway paymentMeans = Factories.persistence.forPaymentMean();

    @Override
    public int[] execute() {
        int generated = 0;
        for (ClientRecord sponsor : clients.findWithInvoicesInState(PAID)) {
            generated += generateVouchersFor(sponsor);
        }
        return new int[] { 0, generated, 0 };
    }

    private int generateVouchersFor(ClientRecord sponsor) {
        List<RecommendationRecord> valid = recommendations.findNotUsedBySponsorId(sponsor.id).stream()
            .filter(recommendation -> hasPaidInvoices(recommendation.recommendedId)).toList();

        int vouchers = valid.size() / RECOMMENDATIONS_PER_VOUCHER;
        for (int i = 0; i < vouchers; i++) {
            paymentMeans.add(newVoucherFor(sponsor));
            valid.subList(i * RECOMMENDATIONS_PER_VOUCHER, (i + 1) * RECOMMENDATIONS_PER_VOUCHER)
                .forEach(this::markAsUsed);
        }
        return vouchers;
    }

    private boolean hasPaidInvoices(String clientId) {
        return invoices.existsByClientIdAndState(clientId, PAID);
    }

    private void markAsUsed(RecommendationRecord recommendation) {
        recommendation.usedForVoucher = true;
        recommendation.updatedAt = LocalDateTime.now();
        recommendations.update(recommendation);
    }

    private VoucherRecord newVoucherFor(ClientRecord client) {
        VoucherRecord voucher = new VoucherRecord();
        voucher.clientId = client.id;
        voucher.code = "V-" + voucher.id.substring(0, 8);
        voucher.description = VOUCHER_DESCRIPTION;
        voucher.available = VOUCHER_AMOUNT;
        return voucher;
    }

}
