package uo.ri.cws.application.service.cashier.crud;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.cws.application.service.cashier.crud.commands.CreateInvoice;
import uo.ri.cws.application.service.cashier.crud.commands.FindNotInvoicedWorkOrders;
import uo.ri.cws.application.service.cashier.crud.commands.SettleInvoice;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.util.exception.BusinessException;

public class CashierCrudServiceImpl implements InvoicingService {

    @Override
    public InvoiceDto create(List<String> workOrderIds) throws BusinessException {
        return new CreateInvoice(workOrderIds).execute();
    }

    @Override
    public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(String nif) throws BusinessException {
        return new FindNotInvoicedWorkOrders(nif).execute();
    }

    @Override
    public void settleInvoice(String invoiceId, Map<String, BigDecimal> charges) throws BusinessException {
        new SettleInvoice(invoiceId, charges).execute();
    }

    // El resto de los métodos de la interfaz deben retornar UnsupportedOperationException si no están implementados todavía
    
    @Override
    public List<InvoicingWorkOrderDto> findWorkOrdersByClientNif(String nif) throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<InvoicingWorkOrderDto> findWorkOrdersByPlateNumber(String plate) throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<InvoiceDto> findInvoiceByNumber(Long number) throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<PaymentMeanDto> findPayMeansByClientNif(String nif) throws BusinessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}