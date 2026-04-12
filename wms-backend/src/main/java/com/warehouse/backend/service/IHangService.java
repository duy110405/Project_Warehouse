package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.HangRequest;
import com.warehouse.backend.dto.response.HangResponse;
import com.warehouse.backend.entity.danhmuc.Hang;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IHangService {
    List<HangResponse> getAllHang();
    HangResponse getHangById(String mah);
    HangResponse saveHang(HangRequest hangRequest);
    HangResponse updateHang( String mah , HangRequest hangRequest);
    void deleteHang(String mah);
    String generateNextMaH() ;
}
