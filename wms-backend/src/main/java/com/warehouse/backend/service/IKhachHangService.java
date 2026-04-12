package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.KhachHangRequest;
import com.warehouse.backend.dto.response.KhachHangResponse;
import com.warehouse.backend.entity.danhmuc.KhachHang;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IKhachHangService {
    List<KhachHangResponse> getAllKhachHang();
    KhachHangResponse getKhachHangById (String makh);
    KhachHangResponse saveKhachHang (KhachHangRequest khachHangRequest);
    KhachHangResponse updateKhachHang(String makh , KhachHangRequest khachHangRequest) ;
    void deleteKhachHang (String makh);
    String generateNextMaKH(); // lệnh tự sinh mã
}
