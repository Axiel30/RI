package uo.ri.cws.application.service.cashier.crud;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.cashier.crud.commands.CreateInvoice;
import uo.ri.cws.application.service.cashier.crud.commands.FindNotInvoicedWorkOrders;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class CashierCrudServiceImpl implements InvoicingService {

	private CommandExecutor executor = new CommandExecutor();

	@Override
	public InvoiceDto create(List<String> workOrderIds) throws BusinessException {
		ArgumentChecks.isNotNull(workOrderIds, "Work order ids list cannot be null");
		ArgumentChecks.isFalse(workOrderIds.isEmpty(), "Work order ids list cannot be empty");
		for (String id : workOrderIds) {
			ArgumentChecks.isNotBlank(id, "Work order id cannot be null or blank");
		}
		return executor.execute(new CreateInvoice(workOrderIds));
	}

	@Override
	public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(String nif) throws BusinessException {
		ArgumentChecks.isNotBlank(nif, "NIF cannot be null or blank");
		return executor.execute(new FindNotInvoicedWorkOrders(nif));
	}

	@Override
	public List<InvoicingWorkOrderDto> findWorkOrdersByClientNif(String nif) throws BusinessException {
		return List.of();
	}

	@Override
	public List<InvoicingWorkOrderDto> findWorkOrdersByPlateNumber(String plate) throws BusinessException {
		return List.of();
	}

	@Override
	public Optional<InvoiceDto> findInvoiceByNumber(Long number) throws BusinessException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public List<PaymentMeanDto> findPayMeansByClientNif(String nif) throws BusinessException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void settleInvoice(String invoiceId, Map<String, BigDecimal> charges) throws BusinessException {
		// TODO Auto-generated method stub
		
	}
}