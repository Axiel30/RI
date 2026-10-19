package uo.ri.cws.application.service.voucher.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.util.exception.BusinessException;

public class GenerateVouchersByRecommendations implements Command<Integer> {

    private ClientGateway cg = Factories.persistence.forClient();
    private RecommendationGateway rg = Factories.persistence.forRecommendation();
    private WorkOrderGateway wog = Factories.persistence.forWorkOrder();
    private InvoiceGateway ig = Factories.persistence.forInvoice();
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();

    @Override
    public Integer execute() throws BusinessException {
        int generatedVouchers = 0;
        List<ClientRecord> clients = cg.findAll();

        for (ClientRecord sponsor : clients) {
            if (!hasPaidWorkOrder(sponsor.id)) {
                continue;
            }

            List<RecommendationRecord> unusedRecs = rg.findNotUsedBySponsorId(sponsor.id);
            List<RecommendationRecord> validRecs = new ArrayList<>();

            for (RecommendationRecord rec : unusedRecs) {
                if (hasPaidWorkOrder(rec.recommended_id)) {
                    validRecs.add(rec);
                }
            }

            int vouchersToCreate = validRecs.size() / 3;
            for (int i = 0; i < vouchersToCreate; i++) {
                VoucherRecord voucher = new VoucherRecord();
                voucher.id = UUID.randomUUID().toString();
                voucher.version = 1L;
                voucher.dtype = "Voucher";
                voucher.client_id = sponsor.id;
                voucher.accumulated = 0.0;
                voucher.available = 25.0;
                voucher.description = "By recommendation";
                voucher.code = "V-" + UUID.randomUUID().toString().substring(0, 8);

                pmg.addVoucher(voucher);

                for (int j = 0; j < 3; j++) {
                    RecommendationRecord usedRec = validRecs.get(i * 3 + j);
                    rg.markAsUsed(usedRec.id);
                }
                generatedVouchers++;
            }
        }
        return generatedVouchers;
    }

    private boolean hasPaidWorkOrder(String clientId) {
        List<WorkOrderRecord> workOrders = wog.findByClientId(clientId);
        for (WorkOrderRecord wo : workOrders) {
            if ("INVOICED".equals(wo.state) && wo.invoice_id != null) {
                Optional<InvoiceRecord> inv = ig.findById(wo.invoice_id);
                if (inv.isPresent() && "PAID".equals(inv.get().state)) {
                    return true;
                }
            }
        }
        return false;
    }
}