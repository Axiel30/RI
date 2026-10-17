package uo.ri.cws.application.service.cashier.crud.commands;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;

public class InvoicingDtoAssembler {

	public static InvoiceRecord toRecord(InvoiceDto dto) {
		InvoiceRecord record = new InvoiceRecord();
		record.id = dto.id;
		record.number = dto.number;
		record.date = dto.date;
		record.subtotal = dto.subtotal;
		record.vatRate = dto.vatPercentage;
		record.vatAmount = dto.vatAmount;
		record.total = dto.total;
		record.state = dto.state;
		record.version = dto.version;
		
		return record;
	}
	public static InvoicingWorkOrderDto toDto(WorkOrderRecord record) {
		InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
		dto.id = record.id;
		dto.description = record.description;
		dto.date = record.date;
		dto.state = record.state;
		dto.amount = record.amount;
		return dto;
	}

	public static List<InvoicingWorkOrderDto> toWorkOrderDtoList(List<WorkOrderRecord> list) {
		List<InvoicingWorkOrderDto> res = new ArrayList<>();
		for (WorkOrderRecord r : list) {
			res.add(toDto(r));
		}
		return res;
	}
}