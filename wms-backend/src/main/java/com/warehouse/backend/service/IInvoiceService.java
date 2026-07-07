package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.InvoiceRequest;
import com.warehouse.backend.dto.response.InvoiceResponse;
import com.warehouse.backend.entity.nghiepvu.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IInvoiceService {
    Page<InvoiceResponse> getInvoices(Integer status, String search, String customerId , Pageable pageable);
    InvoiceResponse getInvoiceById(String invoiceId);
//    List<InvoiceResponse> getAllInvoice();
    InvoiceResponse saveInvoice(InvoiceRequest invoiceRequest);
    InvoiceResponse approveInvoice (String invoiceId);
    InvoiceResponse updateInvoice(String invoiceId , InvoiceRequest invoiceRequest);
    InvoiceResponse cancelInvoice(String invoiceId);
}
