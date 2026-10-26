package uo.ri.cws.application.persistence.paymentmean.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class PaymentMeanGatewayImpl implements PaymentMeanGateway {

    private final PaymentMeanRecordAssembler assembler = new PaymentMeanRecordAssembler();

    @Override
    public void add(PaymentMeanRecord paymentMean) {
        insertPaymentMean(paymentMean);
        if (paymentMean instanceof VoucherRecord voucher) {
            insertVoucher(voucher);
        } else if (paymentMean instanceof CreditCardRecord card) {
            insertCreditCard(card);
        } else {
            execute("TCASHES_ADD", paymentMean.id);
        }
    }

    @Override
    public void remove(String id) {
        execute("TCASHES_REMOVE", id);
        execute("TVOUCHERS_REMOVE", id);
        execute("TCREDITCARDS_REMOVE", id);
        execute("TPAYMENTMEANS_REMOVE", id);
    }

    @Override
    public void update(PaymentMeanRecord paymentMean) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_UPDATE"))) {
            pst.setBigDecimal(1, paymentMean.accumulated);
            pst.setTimestamp(2, Timestamp.valueOf(paymentMean.updatedAt));
            pst.setString(3, paymentMean.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

        if (paymentMean instanceof VoucherRecord voucher) {
            updateVoucher(voucher);
        } else if (paymentMean instanceof CreditCardRecord card) {
            updateCreditCard(card);
        }
    }

    @Override
    public Optional<PaymentMeanRecord> findById(String id) {
        return findOne("TPAYMENTMEANS_FINDBYID", id);
    }

    @Override
    public List<PaymentMeanRecord> findAll() {
        try (
            PreparedStatement pst = Jdbc.getCurrentConnection()
                .prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_FINDALL"));
            ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<PaymentMeanRecord> findByClientId(String clientId) {
        return findMany("TPAYMENTMEANS_FINDBYCLIENTID", clientId);
    }

    @Override
    public Optional<VoucherRecord> findVoucherByCode(String code) {
        return findOne("TVOUCHERS_FINDBYCODE", code).map(VoucherRecord.class::cast);
    }

    @Override
    public Optional<CreditCardRecord> findCreditCardByNumber(String number) {
        return findOne("TCREDITCARDS_FINDBYNUMBER", number).map(CreditCardRecord.class::cast);
    }

    @Override
    public List<VoucherRecord> findVouchersByClientNif(String nif) {
        return findMany("TVOUCHERS_FINDBYCLIENTNIF", nif).stream().map(VoucherRecord.class::cast).toList();
    }

    @Override
    public boolean hasCharges(String paymentMeanId) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TCHARGES_EXISTSBYPAYMENTMEANID"))) {
            pst.setString(1, paymentMeanId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<VoucherRecord> findAllVouchers() {
        try (
            PreparedStatement pst = Jdbc.getCurrentConnection()
                .prepareStatement(Queries.getSQLSentence("TVOUCHERS_FINDALL"));
            ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs).stream().map(VoucherRecord.class::cast).toList();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private void insertVoucher(VoucherRecord voucher) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TVOUCHERS_ADD"))) {
            pst.setString(1, voucher.id);
            pst.setString(2, voucher.code);
            pst.setString(3, voucher.description);
            pst.setBigDecimal(4, voucher.available);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private void insertCreditCard(CreditCardRecord card) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TCREDITCARDS_ADD"))) {
            pst.setString(1, card.id);
            pst.setString(2, card.number);
            pst.setString(3, card.type);
            pst.setDate(4, Date.valueOf(card.validThru));
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private void updateVoucher(VoucherRecord voucher) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TVOUCHERS_UPDATE"))) {
            pst.setString(1, voucher.description);
            pst.setBigDecimal(2, voucher.available);
            pst.setString(3, voucher.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private void updateCreditCard(CreditCardRecord card) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TCREDITCARDS_UPDATE"))) {
            pst.setString(1, card.type);
            pst.setDate(2, Date.valueOf(card.validThru));
            pst.setString(3, card.id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    /** Inserts the row common to every kind of payment mean */
    private void insertPaymentMean(PaymentMeanRecord paymentMean) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection()
            .prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_ADD"))) {
            pst.setString(1, paymentMean.id);
            pst.setString(2, paymentMean.dtype);
            pst.setBigDecimal(3, paymentMean.accumulated);
            pst.setString(4, paymentMean.clientId);
            pst.setLong(5, paymentMean.version);
            pst.setTimestamp(6, Timestamp.valueOf(paymentMean.createdAt));
            pst.setTimestamp(7, Timestamp.valueOf(paymentMean.updatedAt));
            pst.setString(8, paymentMean.entityState);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private void execute(String query, String id) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection().prepareStatement(Queries.getSQLSentence(query))) {
            pst.setString(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Optional<PaymentMeanRecord> findOne(String query, String arg) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection().prepareStatement(Queries.getSQLSentence(query))) {
            pst.setString(1, arg);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toOptionalRecord(rs);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private List<PaymentMeanRecord> findMany(String query, String arg) {
        try (PreparedStatement pst = Jdbc.getCurrentConnection().prepareStatement(Queries.getSQLSentence(query))) {
            pst.setString(1, arg);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
