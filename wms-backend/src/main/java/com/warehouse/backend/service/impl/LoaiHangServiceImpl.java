package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.LoaiHangRequest;
import com.warehouse.backend.dto.response.LoaiHangResponse;
import com.warehouse.backend.entity.danhmuc.LoaiHang;
import com.warehouse.backend.mapper.LoaiHangMapper;
import com.warehouse.backend.repository.LoaiHangRepository;
import com.warehouse.backend.service.ILoaiHangService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoaiHangServiceImpl implements ILoaiHangService {

    private final LoaiHangRepository loaiHangRepository;
    private final LoaiHangMapper loaiHangMapper;
    public LoaiHangServiceImpl(LoaiHangRepository loaiHangRepository, LoaiHangMapper loaiHangMapper){
        this.loaiHangRepository = loaiHangRepository ;
        this.loaiHangMapper = loaiHangMapper;
    }

    public LoaiHang findLoaiHangById(String malh) {return loaiHangRepository.findById(malh)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy loại hàng với mã : " + malh));}

    @Override
    public List<LoaiHangResponse> getAllLoaiHang(){return loaiHangRepository.findAll()
            .stream().map(loaiHangMapper :: toLoaiHangResponse).toList();}

    @Override
    public LoaiHangResponse getLoaiHangById(String malh) {
        LoaiHang loaiHang = findLoaiHangById(malh);
        return loaiHangMapper.toLoaiHangResponse(loaiHang);
    }

    @Override
    public LoaiHangResponse saveLoaiHang(LoaiHangRequest loaiHangRequest){
      // map Dto sang entity
        LoaiHang loaiHang = loaiHangMapper.toLoaiHangEntity(loaiHangRequest);
        // tự sinh mã
        loaiHang.setMalh(generateNextMaLH());
        // lưu xuống db
        LoaiHang savedLoaiHang = loaiHangRepository.save(loaiHang);
        // trả về Dto
        return loaiHangMapper.toLoaiHangResponse(savedLoaiHang);
    }
    @Override
    public LoaiHangResponse updateLoaiHang(String malh , LoaiHangRequest loaiHangRequest){
        // tìm entity cũ trong db
        LoaiHang existingloaiHang = findLoaiHangById(malh);
        // đổ request vào entity cũ
        loaiHangMapper.updateLoaiHangFromRequest(loaiHangRequest , existingloaiHang);
        //lưu và map sang response
        LoaiHang updatedLoaiHang = loaiHangRepository.save(existingloaiHang);
        return loaiHangMapper.toLoaiHangResponse(updatedLoaiHang);

    }

    @Override
    public void deleteLoaiHang (String malh) {
        loaiHangRepository.deleteById(malh);
    }

    @Override
    public String generateNextMaLH(){
        String maxId = loaiHangRepository.findMaxMaLH();
        if(maxId == null ) return "LH001" ;
        int nextNumber = Integer.parseInt(maxId.substring(2)) +1 ;
        return String.format("LH%03d" , nextNumber);
    }
}
