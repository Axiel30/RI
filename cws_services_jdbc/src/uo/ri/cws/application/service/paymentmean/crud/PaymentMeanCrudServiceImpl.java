package uo.ri.cws.application.service.paymentmean.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CashRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class PaymentMeanCrudServiceImpl implements PaymentMeanCrudService {

    private CommandExecutor executor = new CommandExecutor();
    private PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    private ClientGateway cg = Factories.persistence.forClient();

    @Override
    public CardDto addCard(CardDto card) throws BusinessException {
        ArgumentChecks.isNotNull(card, "Card cannot be null");
        ArgumentChecks.isNotBlank(card.clientId, "Client id cannot be blank");
        ArgumentChecks.isNotBlank(card.cardNumber, "Card number cannot be blank");
        ArgumentChecks.isNotBlank(card.cardType, "Card type cannot be blank");
        ArgumentChecks.isNotNull(card.cardExpiration, "Expiration date cannot be null");

        return executor.execute(() -> {
            Optional<ClientRecord> client = cg.findById(card.clientId);
            BusinessChecks.isTrue(client.isPresent(), "Client does not exist");
            BusinessChecks.isTrue(pmg.findCreditCardByNumber(card.cardNumber).isEmpty(),
                    "There is already a credit card with the same number");
//            BusinessChecks.isTrue(card.cardExpiration.isAfter(LocalDate.now()),
//                    "Card expiration date must be in the future");

            CreditCardRecord r = new CreditCardRecord();
            r.id = UUID.randomUUID().toString();
            r.version = 1L;
            r.dtype = "CreditCard";
            r.client_id = card.clientId;
            r.accumulated = 0.0;
            r.number = card.cardNumber;
            r.type = card.cardType;
//            r.validThru = card.cardExpiration;

            pmg.addCreditCard(r);

            card.id = r.id;
            card.version = r.version;
//            card.accumulated = r.accumulated;
            return card;
        });
    }

    @Override
    public VoucherDto addVoucher(VoucherDto voucher) throws BusinessException {
        ArgumentChecks.isNotNull(voucher, "Voucher cannot be null");
        ArgumentChecks.isNotBlank(voucher.clientId, "Client id cannot be blank");
        ArgumentChecks.isNotBlank(voucher.code, "Voucher code cannot be blank");
        ArgumentChecks.isNotBlank(voucher.description, "Voucher description cannot be blank");
//        ArgumentChecks.isNotNull(voucher.available, "Available cannot be null");
//        ArgumentChecks.isTrue(voucher.available >= 0.0, "Available cannot be negative");

        return executor.execute(() -> {
            Optional<ClientRecord> client = cg.findById(voucher.clientId);
            BusinessChecks.isTrue(client.isPresent(), "Client does not exist");
            BusinessChecks.isTrue(pmg.findVoucherByCode(voucher.code).isEmpty(),
                    "There is already a voucher with the same code");

            VoucherRecord r = new VoucherRecord();
            r.id = UUID.randomUUID().toString();
            r.version = 1L;
            r.dtype = "Voucher";
            r.client_id = voucher.clientId;
            r.accumulated = 0.0;
            r.code = voucher.code;
            r.description = voucher.description;
//            r.available = voucher.available;

            pmg.addVoucher(r);

            voucher.id = r.id;
            voucher.version = r.version;
//            voucher.accumulated = r.accumulated;
            return voucher;
        });
    }

    @Override
    public void deletePaymentMean(String id) throws BusinessException {
        ArgumentChecks.isNotBlank(id, "Id cannot be null or blank");

        executor.execute(() -> {
            Optional<PaymentMeanRecord> pmOpt = pmg.findById(id);
            BusinessChecks.isTrue(pmOpt.isPresent(), "Payment mean does not exist");
            PaymentMeanRecord pm = pmOpt.get();

            BusinessChecks.isFalse("Cash".equalsIgnoreCase(pm.dtype) || pmg.findCashById(id).isPresent(),
                    "Cash payment mean cannot be deleted");
            BusinessChecks.isFalse(pmg.hasCharges(id),
                    "Payment mean has charges and cannot be deleted");

            if (pmg.findVoucherById(id).isPresent()) {
                pmg.removeVoucher(id);
            } else if (pmg.findCreditCardById(id).isPresent()) {
                pmg.removeCreditCard(id);
            } else {
                pmg.remove(id);
            }
            return null;
        });
    }

    @Override
    public Optional<PaymentMeanDto> findById(String id) throws BusinessException {
        ArgumentChecks.isNotBlank(id, "Id cannot be null or blank");

        return executor.execute(() -> {
            Optional<VoucherRecord> v = pmg.findVoucherById(id);
            if (v.isPresent()) {
                return Optional.of(toVoucherDto(v.get()));
            }
            Optional<CreditCardRecord> cc = pmg.findCreditCardById(id);
            if (cc.isPresent()) {
                return Optional.of(toCardDto(cc.get()));
            }
            Optional<CashRecord> cash = pmg.findCashById(id);
            if (cash.isPresent()) {
                return Optional.of(toCashDto(cash.get()));
            }
            return Optional.empty();
        });
    }

    @Override
    public List<PaymentMeanDto> findPaymentMeansByClientNif(String nif) throws BusinessException {
        ArgumentChecks.isNotBlank(nif, "NIF cannot be null or blank");

        return executor.execute(() -> {
            Optional<ClientRecord> client = cg.findByNif(nif);
            if (client.isEmpty()) {
                return List.of();
            }
            String clientId = client.get().id;
            List<PaymentMeanDto> res = new ArrayList<>();
            pmg.findCashesByClientId(clientId).forEach(c -> res.add(toCashDto(c)));
            pmg.findCreditCardsByClientId(clientId).forEach(cc -> res.add(toCardDto(cc)));
            pmg.findVouchersByClientId(clientId).forEach(v -> res.add(toVoucherDto(v)));
            return res;
        });
    }

    @Override
    public Optional<CardDto> findCreditCardByNumber(String number) throws BusinessException {
        ArgumentChecks.isNotBlank(number, "Card number cannot be null or blank");

        return executor.execute(() -> pmg.findCreditCardByNumber(number).map(this::toCardDto));
    }

    @Override
    public Optional<VoucherDto> findVoucherByCode(String code) throws BusinessException {
        ArgumentChecks.isNotBlank(code, "Voucher code cannot be null or blank");

        return executor.execute(() -> pmg.findVoucherByCode(code).map(this::toVoucherDto));
    }

    private CardDto toCardDto(CreditCardRecord r) {
        CardDto dto = new CardDto();
        dto.id = r.id;
        dto.version = r.version;
        dto.clientId = r.client_id;
//        dto.accumulated = r.accumulated;
        dto.cardNumber = r.number;
        dto.cardType = r.type;
//        dto.cardExpiration = r.validThru;
        return dto;
    }

    private VoucherDto toVoucherDto(VoucherRecord r) {
        VoucherDto dto = new VoucherDto();
        dto.id = r.id;
        dto.version = r.version;
        dto.clientId = r.client_id;
//        dto.accumulated = r.accumulated;
        dto.code = r.code;
        dto.description = r.description;
//        dto.available = r.available;
        return dto;
    }

    private CashDto toCashDto(CashRecord r) {
        CashDto dto = new CashDto();
        dto.id = r.id;
        dto.version = r.version;
        dto.clientId = r.client_id;
//        dto.accumulated = r.accumulated;
        return dto;
    }
}