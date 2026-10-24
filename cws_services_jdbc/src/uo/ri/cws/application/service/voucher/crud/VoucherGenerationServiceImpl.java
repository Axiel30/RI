package uo.ri.cws.application.service.voucher.crud;

import java.util.List;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.voucher.crud.command.FindVouchersByClientNif;
import uo.ri.cws.application.service.voucher.crud.command.GenerateVouchersByRecommendations;
import uo.ri.cws.application.service.voucher.crud.command.GetVoucherSummary;
import uo.ri.util.exception.BusinessException;

public class VoucherGenerationServiceImpl implements VoucherGenerationService {

	private final CommandExecutor executor = new CommandExecutor();

	/**
	 * Only the vouchers for recommendations are generated, so the amounts of
	 * vouchers for work orders and for big invoices are always zero.
	 */
	@Override
	public int[] generateVouchers() throws BusinessException {
		int forRecommendations = executor.execute(new GenerateVouchersByRecommendations());
		return new int[] { 0, forRecommendations, 0 };
	}

	@Override
	public List<VoucherDto> findVouchersByClientNif(String nif) throws BusinessException {
		return executor.execute(new FindVouchersByClientNif(nif));
	}

	@Override
	public List<VoucherSummaryDto> getVoucherSummary() throws BusinessException {
		return executor.execute(new GetVoucherSummary());
	}

}
