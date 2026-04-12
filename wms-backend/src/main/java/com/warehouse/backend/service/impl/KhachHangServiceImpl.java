package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.KhachHangRequest;
import com.warehouse.backend.dto.response.KhachHangResponse;
import com.warehouse.backend.entity.danhmuc.KhachHang;
import com.warehouse.backend.mapper.KhachHangMapper;
import com.warehouse.backend.repository.KhachHangRepository;
import com.warehouse.backend.service.IKhachHangService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhachHangServiceImpl implements IKhachHangService {
    private final KhachHangRepository khachHangRepository;
    private final KhachHangMapper khachHangMapper; // Khai báo MapStruct

    public KhachHangServiceImpl (KhachHangRepository khachHangRepository , KhachHangMapper khachHangMapper){
        this.khachHangRepository = khachHangRepository ;
        this.khachHangMapper = khachHangMapper;
    }

    // Hàm phụ (private) để lấy Entity nội bộ, tránh lặp lại code
    private KhachHang findEntityKHById(String makh) {
        return khachHangRepository.findById(makh)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với mã: " + makh));
    }

    @Override
    public List<KhachHangResponse> getAllKhachHang (){
        return khachHangRepository.findAll().stream()
                .map(khachHangMapper :: toKhachHangResponse).toList();
    }

    @Override
    public KhachHangResponse getKhachHangById (String makh){
       KhachHang khachHang = findEntityKHById(makh);
               return  khachHangMapper.toKhachHangResponse(khachHang);
    }

    @Override
    public KhachHangResponse saveKhachHang (KhachHangRequest khachHangRequest){
        // 1. Map từ DTO sang Entity
        KhachHang khachHang = khachHangMapper.toKhachHangEntity(khachHangRequest);

        // 2. Tự sinh mã
        khachHang.setMakh(generateNextMaKH());

        // 3. Lưu xuống DB
        KhachHang savedKhachHang = khachHangRepository.save(khachHang);

        // 4. Trả về DTO
        return khachHangMapper.toKhachHangResponse(savedKhachHang);

    }
    @Override
    @Transactional // Đảm bảo tính toàn vẹn dữ liệu khi cập nhật
    public KhachHangResponse updateKhachHang(String makh, KhachHangRequest khachHangRequest) {
        // 1. Tìm Entity cũ trong DB
        KhachHang existingKhachHang = findEntityKHById(makh);

        // 2. Dùng MapStruct đổ tự động các trường từ Request sang Entity cũ
        khachHangMapper.updateKhachHangFromRequest(khachHangRequest, existingKhachHang);

        // 3. Lưu và map sang Response
        KhachHang updatedKhachHang = khachHangRepository.save(existingKhachHang);
        return khachHangMapper.toKhachHangResponse(updatedKhachHang);

    }

    @Override
    public  void deleteKhachHang (String makh){
        khachHangRepository.deleteById(makh);
    }

    @Override
    public String generateNextMaKH() {
        String maxId = khachHangRepository.findMaxMaKH();
        if (maxId == null) return "KH001";
        // Tách phần số: KH005 -> 5
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        // Định dạng lại thành KH + 3 chữ số: KH006
        return String.format("KH%03d", nextNumber);
    }

}
