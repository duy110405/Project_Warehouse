package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.NguyenLieuRequest;
import com.warehouse.backend.dto.response.NguyenLieuResponse;
import com.warehouse.backend.entity.danhmuc.NguyenLieu;
import com.warehouse.backend.mapper.NguyenLieuMapper;
import com.warehouse.backend.repository.NguyenLieuRepository;
import com.warehouse.backend.service.INguyenLieuService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NguyeLieuServiceImpl implements INguyenLieuService {
    private final NguyenLieuRepository nguyenLieuRepository ;
    private final NguyenLieuMapper nguyenLieuMapper ;
    public NguyeLieuServiceImpl (NguyenLieuRepository nguyenLieuRepository , NguyenLieuMapper nguyenLieuMapper){
        this.nguyenLieuRepository = nguyenLieuRepository;
        this.nguyenLieuMapper = nguyenLieuMapper;
    }

    private NguyenLieu getNlById(String manl) {return nguyenLieuRepository.findById(manl)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyeen liệu với mã :"+ manl));}

    @Override
    public List<NguyenLieuResponse> getAllNguyenLieu (){
        return nguyenLieuRepository.findAll().stream().map(nguyenLieuMapper :: toNguyenLieuResponse)
                .toList();
    }

    @Override
    public NguyenLieuResponse getNguyenLieuById(String manl) {
       NguyenLieu nguyenLieu = getNlById(manl);
       return nguyenLieuMapper.toNguyenLieuResponse(nguyenLieu);
    }

    @Override
    public NguyenLieuResponse saveNguyenLieu (NguyenLieuRequest nguyenLieuRequest){
        NguyenLieu nguyenLieu = nguyenLieuMapper.toNguyenLieuEntity(nguyenLieuRequest);
        nguyenLieu.setManl(generateNextMaNL());
        NguyenLieu savedNguyenLieu = nguyenLieuRepository.save(nguyenLieu);
        return nguyenLieuMapper.toNguyenLieuResponse(savedNguyenLieu);
    }

    @Override
    @Transactional
    public NguyenLieuResponse updateNguyenLieu (String manl , NguyenLieuRequest nguyenLieuRequest){
      NguyenLieu existingNguyenLieu = getNlById(manl);
      nguyenLieuMapper.updateNguyenLieuFromRequset(nguyenLieuRequest , existingNguyenLieu);
      NguyenLieu updateNguyenLieu = nguyenLieuRepository.save(existingNguyenLieu);
      return nguyenLieuMapper.toNguyenLieuResponse(updateNguyenLieu);

    }

    @Override
    public void deleteNguyenLieu (String manl) {
       nguyenLieuRepository.deleteById(manl);
    }

    @Override
    public String generateNextMaNL(){
        String maxId = nguyenLieuRepository.findMaxMaNL();
        if(maxId == null ) return "NL001" ;
        int nextNumber = Integer.parseInt(maxId.substring(2)) +1 ;
        return String.format("NL%03d" , nextNumber);
    }
}
