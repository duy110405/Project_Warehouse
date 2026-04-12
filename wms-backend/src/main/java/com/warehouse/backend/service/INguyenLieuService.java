package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.NguyenLieuRequest;
import com.warehouse.backend.dto.response.NguyenLieuResponse;
import com.warehouse.backend.entity.danhmuc.NguyenLieu;

import java.util.List;

public interface INguyenLieuService {

    List<NguyenLieuResponse> getAllNguyenLieu();
    NguyenLieuResponse getNguyenLieuById(String manl);
    NguyenLieuResponse saveNguyenLieu(NguyenLieuRequest nguyenLieuRequest);
    NguyenLieuResponse updateNguyenLieu(String manl , NguyenLieuRequest nguyenLieuRequest);
    void deleteNguyenLieu(String manl);
    String generateNextMaNL();
}
