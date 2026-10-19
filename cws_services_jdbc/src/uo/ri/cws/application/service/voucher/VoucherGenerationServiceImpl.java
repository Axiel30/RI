package uo.ri.cws.application.service.voucher;

import java.util.List;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.voucher.commands.FindVouchersByClientNif;
import uo.ri.cws.application.service.voucher.commands.GenerateVouchersByRecommendations;
import uo.ri.cws.application.service.voucher.commands.GetVoucherSummary;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class VoucherGenerationServiceImpl implements VoucherGenerationService {

    private CommandExecutor executor = new CommandExecutor();

    @Override
    public int[] generateVouchers() throws BusinessException {
        int generated25 = executor.execute(new GenerateVouchersByRecommendations());
        return new int[] { 0, generated25, 0 };
    }

    @Override
    public List<VoucherDto> findVouchersByClientNif(String nif) throws BusinessException {
        ArgumentChecks.isNotBlank(nif, "NIF cannot be null or blank");
        return executor.execute(new FindVouchersByClientNif(nif));
    }

    @Override
    public List<VoucherSummaryDto> getVoucherSummary() throws BusinessException {
        return executor.execute(new GetVoucherSummary());
    }
}