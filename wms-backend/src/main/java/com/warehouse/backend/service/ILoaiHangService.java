package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.LoaiHangRequest;
import com.warehouse.backend.dto.response.LoaiHangResponse;
import com.warehouse.backend.entity.danhmuc.LoaiHang;
import com.warehouse.backend.service.impl.LoaiHangServiceImpl;

import java.util.List;

public interface ILoaiHangService {

    List<LoaiHangResponse> getAllLoaiHang();
    LoaiHangResponse getLoaiHangById(String malh);
    LoaiHangResponse saveLoaiHang(LoaiHangRequest loaiHangRequest);
    LoaiHangResponse updateLoaiHang(String malh , LoaiHangRequest loaiHangRequest);
    void deleteLoaiHang(String malh);
    String generateNextMaLH() ;
}
