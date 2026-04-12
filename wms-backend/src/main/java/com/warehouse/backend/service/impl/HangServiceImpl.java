package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.HangRequest;
import com.warehouse.backend.dto.response.HangResponse;
import com.warehouse.backend.entity.danhmuc.Hang;
import com.warehouse.backend.entity.danhmuc.LoaiHang;
import com.warehouse.backend.entity.danhmuc.NL_H;
import com.warehouse.backend.entity.danhmuc.NguyenLieu;
import com.warehouse.backend.mapper.HangMapper;
import com.warehouse.backend.repository.HangRepository;
import com.warehouse.backend.repository.LoaiHangRepository;
import com.warehouse.backend.repository.NL_HRepository;
import com.warehouse.backend.repository.NguyenLieuRepository;
import com.warehouse.backend.service.IHangService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HangServiceImpl implements IHangService {

    private final HangRepository hangRepository;
    private final NguyenLieuRepository nguyenLieuRepository;
    private final NL_HRepository nlHRepository;
    private final LoaiHangRepository loaiHangRepository;
    private final HangMapper hangMapper ;


    public HangServiceImpl(HangRepository hangRepository,
                           NguyenLieuRepository nguyenLieuRepository,
                           NL_HRepository nlHRepository, LoaiHangRepository loaiHangRepository, HangMapper hangMapper) {
        this.hangRepository = hangRepository;
        this.nguyenLieuRepository = nguyenLieuRepository;
        this.nlHRepository = nlHRepository;
        this.loaiHangRepository = loaiHangRepository;
        this.hangMapper = hangMapper;
    }

    public Hang findHangById(String mah){
        return hangRepository.findById(mah)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng với mã : " + mah));
    }

    @Override
    public List<HangResponse> getAllHang(){
        return hangRepository.findAll()
                .stream().map(hangMapper :: toHangResponse).toList();
    }

    @Override
    public HangResponse getHangById(String mah){
        Hang hang = findHangById(mah);
        return hangMapper.toHangResponse(hang);
    }

    @Override
    @Transactional
    public HangResponse saveHang(HangRequest hangRequest) {
        // Map DTO sang entity
        Hang hang = hangMapper.toHangEntity(hangRequest);
        // tự sinh mã
        hang.setMah(generateNextMaH());
        //  Xử lý tìm và gán Loại Hàng trước khi lưu
        if (hangRequest.getMaloai() != null) {
            LoaiHang loaiHang = loaiHangRepository.findById(hangRequest.getMaloai())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy loại hàng với mã: " + hangRequest.getMaloai()));
            // Gán vào Entity Hàng
            hang.setLoaiHang(loaiHang);
        }
        // lưu xuống db
        Hang savedHang = hangRepository.save(hang);
        // Xử lý lưu bảng NL_H
        if (hangRequest.getDanhSachMaNL() != null && !hangRequest.getDanhSachMaNL().isEmpty()) {
            for (String manl : hangRequest.getDanhSachMaNL()) {
                NguyenLieu nl = nguyenLieuRepository.findById(manl).orElseThrow();
                NL_H nlh = new NL_H();
                nlh.setHang(savedHang);
                nlh.setNguyenLieu(nl);
                nlHRepository.save(nlh);
                savedHang.getChiTietNguyenLieu().add(nlh);
            }
        }
        // Trả về response
        return hangMapper.toHangResponse(savedHang);
    }

    @Override
    @Transactional
    public HangResponse updateHang(String mah, HangRequest hangRequest) {

        // Tìm Hàng cũ trong DB
        Hang existingHang = hangRepository.findById(mah)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Hàng với mã: " + mah));

        // dùng mapper cập nhật thông tin
        hangMapper.updateHangFromRequset(hangRequest , existingHang);
        //Cập nhật Loại Hàng nếu có thay đổi
        if (hangRequest.getMaloai() != null) {
            LoaiHang loaiHang = loaiHangRepository.findById(hangRequest.getMaloai())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy loại hàng với mã: " + hangRequest.getMaloai()));
            existingHang.setLoaiHang(loaiHang);
        }

        Hang updatedHang = hangRepository.save(existingHang);

        //Xóa NL cũ - Thêm NL mới
        if (hangRequest.getDanhSachMaNL() != null) {
            nlHRepository.deleteByHang_Mah(mah);
            for (String manl : hangRequest.getDanhSachMaNL()) {
                NguyenLieu nl = nguyenLieuRepository.findById(manl).orElseThrow();
                NL_H nlh = new NL_H();
                nlh.setHang(updatedHang);
                nlh.setNguyenLieu(nl);
                nlHRepository.save(nlh);
            }
        }

        Hang finalHang = hangRepository.findById(mah).get();
        return hangMapper.toHangResponse(finalHang);

    }

    @Transactional
    @Override
    public void deleteHang(String mah) {
        //  Xóa hết các bản ghi liên quan trong bảng con NL_H trước
        nlHRepository.deleteByHang_Mah(mah);

        // Sau đó mới xóa an toàn ở bảng cha HANG
        hangRepository.deleteById(mah);
    }

    @Override
    public String generateNextMaH(){
        String maxId = hangRepository.findMaxMaH();
        if (maxId == null) return "H001";
        int nextNumber = Integer.parseInt(maxId.substring(1)) + 1;
        return String.format("H%03d", nextNumber);
    }
}
