package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.InvoiceRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.InvoiceResponse;
import com.warehouse.backend.service.IInvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
@CrossOrigin("*") // Cho phép React gọi API
public class InvoiceController {
    private final IInvoiceService invoiceService;

    public InvoiceController(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ApiResponse<List<InvoiceResponse>> getAllInvoice() {
        return ApiResponse.<List<InvoiceResponse>>builder()
                .code(200)
                .message("Lấy danh sách hóa đơn thành công!")
                .data(invoiceService.getAllInvoice())
                .build();
    }

    @GetMapping("/{invoiceId}")
    public ApiResponse<InvoiceResponse> getInvoiceById(@PathVariable String invoiceId) {
        return ApiResponse.<InvoiceResponse>builder()
                .code(200)
                .message("Tìm thấy hóa đơn")
                .data(invoiceService.getInvoiceById(invoiceId))
                .build();
    }

    @PostMapping
    public ApiResponse<InvoiceResponse> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        return ApiResponse.<InvoiceResponse>builder()
                .code(201)
                .message("Thêm hóa đơn thành công!")
                .data(invoiceService.saveInvoice(invoiceRequest))
                .build();
    }

    @PutMapping("/{invoiceId}/approve")
    public ApiResponse<InvoiceResponse> approveInvoice(@PathVariable String invoiceId) {
        return ApiResponse.<InvoiceResponse>builder()
                .code(200)
                .message("Duyệt hóa đơn thành công!")
                .data(invoiceService.approveInvoice(invoiceId))
                .build();
    }

    @PutMapping("/{invoiceId}/cancel")
    public ApiResponse<InvoiceResponse> cancelInvoice(@PathVariable String invoiceId) {
        return ApiResponse.<InvoiceResponse>builder()
                .code(200)
                .message("Hủy hóa đơn thành công!")
                .data(invoiceService.cancelInvoice(invoiceId))
                .build();
    }

    @PutMapping("/{invoiceId}")
    public ApiResponse<InvoiceResponse> updateInvoice(@PathVariable String invoiceId, @RequestBody InvoiceRequest invoiceRequest) {
        return ApiResponse.<InvoiceResponse>builder()
                .code(200)
                .message("Cập nhật hóa đơn thành công!")
                .data(invoiceService.updateInvoice(invoiceId, invoiceRequest))
                .build();
    }
}
