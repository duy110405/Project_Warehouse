package com.warehouse.backend.service;

import com.warehouse.backend.entity.danhmuc.Xuong;

import java.util.List;

public interface IXuongService {
    List<Xuong> getAllXuong();
    Xuong getXuongById (String maxuong) ;
    Xuong saveXuong(Xuong xuong) ;
    Xuong updateXuong (String maxuong , Xuong xuong) ;
    void deleteXuong(String maxuong);
    String generateNextMaX();
}
