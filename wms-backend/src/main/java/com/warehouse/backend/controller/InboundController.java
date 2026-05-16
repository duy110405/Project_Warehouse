package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.InboundReceiptRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import com.warehouse.backend.service.IInboundService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbound")
public class InboundController {
    private final IInboundService inboundService;

    public InboundController(IInboundService inboundService) {
        this.inboundService = inboundService;
    }

    @GetMapping
    public ApiResponse<List<InboundReceiptResponse>> getReceipts(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String supplierId
    ) {
        // Xử lý chuỗi rỗng
        String finalSearch = (search == null || search.trim().isEmpty()) ? null : search.trim();
        String finalSupplier = (supplierId == null || supplierId.trim().isEmpty()) ? null : supplierId.trim();

        // Gọi hàm search có lọc status
        List<InboundReceiptResponse> data = inboundService.searchReceipts(status, finalSearch, finalSupplier);

        return ApiResponse.<List<InboundReceiptResponse>>builder()
                .code(200)
                .message("Lấy danh sách thành công")
                .data(data)
                .build();
    }

    @GetMapping("/{receiptId}")
    public ApiResponse<InboundReceiptResponse> getInboundReceiptById(@PathVariable String receiptId){
        return ApiResponse.<InboundReceiptResponse>builder()
                .code(200)
                .message("Tìm thấy phiếu nhập")
                .data(inboundService.getInboundReceiptById(receiptId))
                .build();
    }

    @PostMapping
    public ApiResponse<InboundReceiptResponse> createInboundReceipt(@Valid @RequestBody InboundReceiptRequest inboundReceiptRequest){
        return ApiResponse.<InboundReceiptResponse>builder()
                .code(201)
                .message("Thêm phiếu nhập thành công!")
                .data(inboundService.saveInboundReceipt(inboundReceiptRequest))
                .build();
    }
    @PutMapping("/{receiptId}/approve")
    public ApiResponse<InboundReceiptResponse> approveInboundReceipt(@PathVariable String receiptId ){
        return ApiResponse.<InboundReceiptResponse>builder()
                .code(200)
                .message("duyệt phiếu nhập thành công!")
                .data(inboundService.approveInboundReceipt(receiptId))
                .build();
    }

    @PutMapping("/{receiptId}/cancel")
    public ApiResponse<InboundReceiptResponse> cancelInboundReceipt(@PathVariable String receiptId){
        return ApiResponse.<InboundReceiptResponse>builder()
                .code(200)
                .message("Hủy phiếu thành công ")
                .data(inboundService.cancelInboundReceipt(receiptId))
                .build();
    }
    @PutMapping("/{receiptId}")
    public ApiResponse<InboundReceiptResponse> updateInboundReceipt(@PathVariable String receiptId, @Valid @RequestBody InboundReceiptRequest inboundReceiptRequest){
        return ApiResponse.<InboundReceiptResponse>builder()
                .code(200)
                .message("Cập nhật phiếu nhập thành công!")
                .data(inboundService.updateInboundReceipt(receiptId , inboundReceiptRequest))
                .build();
    }

}
