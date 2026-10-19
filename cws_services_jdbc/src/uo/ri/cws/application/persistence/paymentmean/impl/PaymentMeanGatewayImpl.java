package uo.ri.cws.application.persistence.paymentmean.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.util.jdbc.Jdbc;
import uo.ri.cws.application.persistence.util.jdbc.Queries;
import uo.ri.util.exception.PersistenceException;

public class PaymentMeanGatewayImpl implements PaymentMeanGateway {

    @Override
    public void add(PaymentMeanRecord t) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_INSERT"))) {
            pst.setString(1, t.id);
            pst.setString(2, t.dtype);
            pst.setDouble(3, t.accumulated);
            pst.setString(4, t.client_id);
            pst.setLong(5, t.version > 0 ? t.version : 1L);
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            pst.setTimestamp(6, now);
            pst.setTimestamp(7, now);
            pst.setString(8, "ENABLED");
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void addVoucher(VoucherRecord v) {
        v.dtype = "Voucher";
        add(v);
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_INSERT"))) {
            pst.setString(1, v.id);
            pst.setDouble(2, v.available);
            pst.setString(3, v.code);
            pst.setString(4, v.description);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void addCreditCard(CreditCardRecord card) {
        card.dtype = "CreditCard";
        add(card);
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_INSERT"))) {
            pst.setString(1, card.id);
            pst.setString(2, card.number);
            pst.setString(3, card.type);
            pst.setDate(4, Date.valueOf(card.validThru));
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_DELETE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void removeVoucher(String id) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_DELETE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        remove(id);
    }

    @Override
    public void removeCreditCard(String id) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_DELETE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        remove(id);
    }

    @Override
    public void update(PaymentMeanRecord t) {
    }

    @Override
    public Optional<PaymentMeanRecord> findById(String id) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_FINDBYID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(fillBase(new PaymentMeanRecord(), rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<PaymentMeanRecord> findAll() {
        return List.of();
    }

    @Override
    public List<PaymentMeanRecord> findByClientId(String clientId) {
        Connection c = Jdbc.getCurrentConnection();
        List<PaymentMeanRecord> res = new ArrayList<>();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TPAYMENTMEANS_FINDBYCLIENTID"))) {
            pst.setString(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(fillBase(new PaymentMeanRecord(), rs));
                }
            }
            return res;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<VoucherRecord> findVoucherById(String id) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_FINDBYID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? Optional.of(toVoucherRecord(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<VoucherRecord> findVoucherByCode(String code) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_FINDBYCODE"))) {
            pst.setString(1, code);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? Optional.of(toVoucherRecord(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<VoucherRecord> findVouchersByClientId(String clientId) {
        Connection c = Jdbc.getCurrentConnection();
        List<VoucherRecord> res = new ArrayList<>();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_FINDBYCLIENTID"))) {
            pst.setString(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(toVoucherRecord(rs));
                }
            }
            return res;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<VoucherRecord> findVouchersByClientNif(String nif) {
        Connection c = Jdbc.getCurrentConnection();
        List<VoucherRecord> res = new ArrayList<>();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TVOUCHERS_FINDBYCLIENTNIF"))) {
            pst.setString(1, nif);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(toVoucherRecord(rs));
                }
            }
            return res;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<CreditCardRecord> findCreditCardById(String id) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_FINDBYID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? Optional.of(toCreditCardRecord(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<CreditCardRecord> findCreditCardByNumber(String number) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_FINDBYNUMBER"))) {
            pst.setString(1, number);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? Optional.of(toCreditCardRecord(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<CreditCardRecord> findCreditCardsByClientId(String clientId) {
        Connection c = Jdbc.getCurrentConnection();
        List<CreditCardRecord> res = new ArrayList<>();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCREDITCARDS_FINDBYCLIENTID"))) {
            pst.setString(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(toCreditCardRecord(rs));
                }
            }
            return res;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<CashRecord> findCashById(String id) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCASHES_FINDBYID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? Optional.of(fillBase(new CashRecord(), rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<CashRecord> findCashesByClientId(String clientId) {
        Connection c = Jdbc.getCurrentConnection();
        List<CashRecord> res = new ArrayList<>();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCASHES_FINDBYCLIENTID"))) {
            pst.setString(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(fillBase(new CashRecord(), rs));
                }
            }
            return res;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean hasCharges(String paymentMeanId) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(Queries.getSQLSentence("TCHARGES_FINDBYPAYMENTMEANID"))) {
            pst.setString(1, paymentMeanId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private <T extends PaymentMeanRecord> T fillBase(T r, ResultSet rs) throws SQLException {
        r.id = rs.getString("id");
        r.version = rs.getLong("version");
        r.dtype = rs.getString("dtype");
        r.accumulated = rs.getDouble("accumulated");
        r.client_id = rs.getString("client_id");
        return r;
    }

    private VoucherRecord toVoucherRecord(ResultSet rs) throws SQLException {
        VoucherRecord r = fillBase(new VoucherRecord(), rs);
        r.available = rs.getDouble("available");
        r.code = rs.getString("code");
        r.description = rs.getString("description");
        return r;
    }

    private CreditCardRecord toCreditCardRecord(ResultSet rs) throws SQLException {
        CreditCardRecord r = fillBase(new CreditCardRecord(), rs);
        r.number = rs.getString("number");
        r.type = rs.getString("type");
        r.validThru = rs.getDate("validThru").toLocalDate();
        return r;
    }
}