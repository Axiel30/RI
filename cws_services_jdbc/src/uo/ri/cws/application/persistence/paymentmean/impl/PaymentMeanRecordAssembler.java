package uo.ri.cws.application.persistence.paymentmean.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CashRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.VoucherRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

/**
 * Builds the right kind of payment mean for each row, depending on its
 * discriminator. The result set must include the columns of the voucher and
 * credit card tables (see the queries).
 */
class PaymentMeanRecordAssembler extends BaseRecordAssembler<PaymentMeanRecord> {

    @Override
    protected PaymentMeanRecord newRecord(ResultSet rs) throws SQLException {
        return switch (rs.getString("dtype")) {
        case "Voucher" -> new VoucherRecord();
        case "CreditCard" -> new CreditCardRecord();
        default -> new CashRecord();
        };
    }

    @Override
    protected void fill(PaymentMeanRecord record, ResultSet rs) throws SQLException {
        record.clientId = rs.getString("client_id");
        record.accumulated = rs.getBigDecimal("accumulated");

        if (record instanceof VoucherRecord voucher) {
            voucher.code = rs.getString("code");
            voucher.description = rs.getString("description");
            voucher.available = rs.getBigDecimal("available");

        } else if (record instanceof CreditCardRecord card) {
            card.number = rs.getString("number");
            card.type = rs.getString("type");
            card.validThru = rs.getDate("validThru").toLocalDate();
        }
    }

}
