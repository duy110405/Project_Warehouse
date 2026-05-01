package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.NhapKhoRequest;
import com.warehouse.backend.dto.response.NhapKhoResponse;

import java.util.List;

public interface INhapKhoService {
    String generateNextMaPN();
    List<NhapKhoResponse> getAllPhieuNhap();
    NhapKhoResponse getPhieuNhapById(String mapnhap);
    NhapKhoResponse savePhieuNhap(NhapKhoRequest nhapKhoRequest);
    NhapKhoResponse duyetPhieuNhap (String mapnhap);
    NhapKhoResponse updatePhieuNhap (String mapnhap , NhapKhoRequest nhapKhoRequest);
    void deletePhieuNhap(String mapnhap);

}
