package uo.ri.cws.application.service.cashier.crud.commands;

import java.util.List;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;

class InvoicingDtoAssembler {

	static InvoiceDto toDto(InvoiceRecord record) {
		InvoiceDto dto = new InvoiceDto();
		dto.id = record.id;
		dto.version = record.version;
		dto.number = record.number;
		dto.date = record.date;
		dto.subtotal = record.subtotal;
		dto.vatPercentage = record.vatRate;
		dto.vatAmount = record.vatAmount;
		dto.total = record.total;
		dto.state = record.state;
		return dto;
	}

	static InvoicingWorkOrderDto toDto(WorkOrderRecord record) {
		InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
		dto.id = record.id;
		dto.description = record.description;
		dto.date = record.date;
		dto.state = record.state;
		dto.amount = record.amount;
		return dto;
	}

	static List<InvoicingWorkOrderDto> toWorkOrderDtoList(List<WorkOrderRecord> records) {
		return records.stream().map(InvoicingDtoAssembler::toDto).toList();
	}

}
