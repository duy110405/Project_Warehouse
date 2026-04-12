package com.warehouse.backend.service.impl;

import com.warehouse.backend.entity.danhmuc.Hang;
import com.warehouse.backend.entity.danhmuc.Xuong;
import com.warehouse.backend.repository.HangRepository;
import com.warehouse.backend.repository.XuongRepository;
import com.warehouse.backend.service.IXuongService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XuongServiceImpl implements IXuongService {
    private final XuongRepository xuongRepository;

    public XuongServiceImpl (XuongRepository xuongRepository){
        this.xuongRepository = xuongRepository;
    }

    @Override
    public List<Xuong> getAllXuong(){return xuongRepository.findAll();}

    @Override
    public Xuong getXuongById(String maxuong){return xuongRepository.findById(maxuong)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy xường mã :" + maxuong));}

    @Override
    public Xuong saveXuong(Xuong xuong){
        if(xuong.getMaxuong() == null || xuong.getMaxuong().isEmpty()){
            xuong.setMaxuong(generateNextMaX());
        }
        return xuongRepository.save(xuong);
    }

    @Override
    @Transactional
    public Xuong updateXuong(String maxuong , Xuong xuongRequest){
        Xuong  existingXuong  = getXuongById(maxuong);

        existingXuong.setTenxuong(xuongRequest.getTenxuong());

        return xuongRepository.save(existingXuong);
    }

    @Override
    public void deleteXuong(String maxuong) {
        xuongRepository.deleteById(maxuong);
    }

    @Override
    public String generateNextMaX(){
        String maxId = xuongRepository.findMaxMaXuong();
        if (maxId == null) return "X001";
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        return String.format("X%03d", nextNumber);
    }
}
