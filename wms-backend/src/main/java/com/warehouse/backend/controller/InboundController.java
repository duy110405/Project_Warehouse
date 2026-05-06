package com.warehouse.backend.controller;

import com.warehouse.backend.dto.request.InboundReceiptRequest;
import com.warehouse.backend.dto.response.ApiResponse;
import com.warehouse.backend.dto.response.InboundReceiptResponse;
import com.warehouse.backend.service.IInboundService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbound")
@CrossOrigin("*") // Cho phép React gọi API
public class InboundController {
    private final IInboundService inboundService;

    public InboundController(IInboundService inboundService) {
        this.inboundService = inboundService;
    }

    @GetMapping
    public ApiResponse<List<InboundReceiptResponse >> getAllInboundReceipt(){
        return ApiResponse.<List<InboundReceiptResponse>>builder()
                .code(200)
                .message("Lấy danh sách phiếu nhập thành công!")
                .data(inboundService.getAllInboundReceipt())
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
    public ApiResponse<InboundReceiptResponse> createInboundReceipt(@RequestBody InboundReceiptRequest inboundReceiptRequest){
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
    public ApiResponse<InboundReceiptResponse> updateInboundReceipt(@PathVariable String receiptId, @RequestBody InboundReceiptRequest inboundReceiptRequest){
        return ApiResponse.<InboundReceiptResponse>builder()
                .code(200)
                .message("Cập nhật phiếu nhập thành công!")
                .data(inboundService.updateInboundReceipt(receiptId , inboundReceiptRequest))
                .build();
    }

}
