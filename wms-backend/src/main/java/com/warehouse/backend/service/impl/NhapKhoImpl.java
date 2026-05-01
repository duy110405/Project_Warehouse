package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.DNhapKhoRequest;
import com.warehouse.backend.dto.request.NhapKhoRequest;
import com.warehouse.backend.dto.response.HangResponse;
import com.warehouse.backend.dto.response.NhapKhoResponse;
import com.warehouse.backend.entity.danhmuc.Hang;
import com.warehouse.backend.entity.danhmuc.Xuong;
import com.warehouse.backend.entity.nghiepvu.DNhapKho;
import com.warehouse.backend.entity.nghiepvu.PhieuNhap;
import com.warehouse.backend.mapper.NhapKhoMapper;
import com.warehouse.backend.repository.DNhapKhoRepository;
import com.warehouse.backend.repository.HangRepository;
import com.warehouse.backend.repository.PhieuNhapRepository;
import com.warehouse.backend.repository.XuongRepository;
import com.warehouse.backend.service.INhapKhoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NhapKhoImpl implements INhapKhoService {
    private final NhapKhoMapper nhapKhoMapper;
    private final PhieuNhapRepository phieuNhapRepository;
    private final DNhapKhoRepository dNhapKhoRepository;
    private final HangRepository hangRepository;
    private final XuongRepository xuongRepository;


    public NhapKhoImpl(NhapKhoMapper nhapKhoMapper, PhieuNhapRepository phieuNhapRepository, DNhapKhoRepository dNhapKhoRepository, HangRepository hangRepository, XuongRepository xuongRepository) {
        this.nhapKhoMapper = nhapKhoMapper;
        this.phieuNhapRepository = phieuNhapRepository;
        this.dNhapKhoRepository = dNhapKhoRepository;
        this.hangRepository = hangRepository;
        this.xuongRepository = xuongRepository;
    }

    public PhieuNhap findPhieuNhapById(String mapnhap){
        return phieuNhapRepository.findById(mapnhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập với mã : " + mapnhap));
    }
    @Override
    public String generateNextMaPN(){
        String maxId = phieuNhapRepository.findMaxMaPN();
        if (maxId == null) return "PN001";
        int nextNumber = Integer.parseInt(maxId.substring(2)) + 1;
        return String.format("PN%03d", nextNumber);
    }

    @Override
    public List<NhapKhoResponse> getAllPhieuNhap(){
        return phieuNhapRepository.findAll()
                .stream().map(nhapKhoMapper :: toNhapKhoResponse).toList();
    }

    @Override
    public NhapKhoResponse getPhieuNhapById(String mapnhap){
        PhieuNhap phieuNhap = findPhieuNhapById(mapnhap);
        return nhapKhoMapper.toNhapKhoResponse(phieuNhap);
    }

    @Override
    @Transactional
     public NhapKhoResponse savePhieuNhap(NhapKhoRequest nhapKhoRequest){
        if (nhapKhoRequest.getChiTietNhapKho() == null || nhapKhoRequest.getChiTietNhapKho().isEmpty()) {
            throw new RuntimeException("phải có ít nhất 1 hàng ");
        }
        // Map DTO sang entity
      PhieuNhap phieuNhap = nhapKhoMapper.toPhieuNhapEntity(nhapKhoRequest);
        // tự sinh mã
        phieuNhap.setMapnhap(generateNextMaPN());
        phieuNhap.setNgaynhap(LocalDate.now());

        // Mock Data người dùng (Sau này thay bằng Spring Security)
        phieuNhap.setTennguoinhap("Admin Lập Phiếu");
        phieuNhap.setDonvi("Quan lý kho A");

        // Tìm Xưởng và gán vào Phiếu
        Xuong xuong = xuongRepository.findById(nhapKhoRequest.getMaXuong())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Xưởng!"));
        phieuNhap.setXuong(xuong);

        // xử lý Chi tiết nhập kho
        List<DNhapKho> danhSachChiTiet = new ArrayList<>();
        BigDecimal tongTienPhieu = BigDecimal.ZERO;
        for (DNhapKhoRequest ctRequest : nhapKhoRequest.getChiTietNhapKho()) {
            Hang hang = hangRepository.findById(ctRequest.getMaH())
                    .orElseThrow(() -> new RuntimeException("Hàng hóa không tồn tại!"));

        // Tạo dòng chi tiết
            DNhapKho dNhapKho = new DNhapKho();
            dNhapKho.setPhieuNhap(phieuNhap);
            dNhapKho.setHang(hang);
            dNhapKho.setSoluong(ctRequest.getSoLg());
            dNhapKho.setGia(ctRequest.getGia());

       // tính tổng tiền
       BigDecimal thanhTienDong = dNhapKho.getThanhTien();
            // Cộng dồn vào Tổng tiền của tờ phiếu
            tongTienPhieu = tongTienPhieu.add(thanhTienDong);
            // Bỏ dòng này vào giỏ
            danhSachChiTiet.add(dNhapKho);
    }
        phieuNhap.setTongtien(tongTienPhieu);
        phieuNhap.setChiTietPhieuNhap(danhSachChiTiet);
        PhieuNhap savedPhieuNhap = phieuNhapRepository.save(phieuNhap);
        return nhapKhoMapper.toNhapKhoResponse(savedPhieuNhap);
    }

    @Override
    @Transactional
    public NhapKhoResponse duyetPhieuNhap (String mapnhap) {
        // tìm phiếu nhập
        PhieuNhap phieuNhap = phieuNhapRepository.findById(mapnhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập với mã: " + mapnhap));
        if(phieuNhap.getTrangThai() != 0 ){
            throw new RuntimeException("Phiếu nhập đã được duyệt hoặc đã hủy, không thể duyệt lại!");
        }
            phieuNhap.setTrangThai(1); // Cập nhật trạng thái thành "Đã duyệt"
            for (DNhapKho chiTiet : phieuNhap.getChiTietPhieuNhap()) {
                Hang hang = chiTiet.getHang();
                // Cộng dồn kho
                int soLuongMoi = hang.getSoluong() + chiTiet.getSoluong();
                hang.setSoluong(soLuongMoi);
                // Cập nhật Hàng xuống DB
                hangRepository.save(hang);
            }
        PhieuNhap savedPhieuNhap = phieuNhapRepository.save(phieuNhap);
        return nhapKhoMapper.toNhapKhoResponse(savedPhieuNhap);
    }

    @Override
    @Transactional
    public NhapKhoResponse updatePhieuNhap (String mapnhap , NhapKhoRequest nhapKhoRequest){
        // Tìm phiếu cũ trong db và phiếu phải ở trạng thái chưa duyệt
        PhieuNhap existingPhieuNhap = phieuNhapRepository.findById(mapnhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập với mã: " + mapnhap));
        if(existingPhieuNhap.getTrangThai() != 0 ) {
            throw new RuntimeException("Phiếu nhập đã được duyệt hoặc đã hủy, không thể cập nhật!");
        }
            nhapKhoMapper.updateNhapKhoFromRequest(nhapKhoRequest , existingPhieuNhap);
        // Cập nhật lại Xưởng nếu FE có gửi Xưởng mới
        if(nhapKhoRequest.getMaXuong() != null) {
            Xuong xuong = xuongRepository.findById(nhapKhoRequest.getMaXuong())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Xưởng!"));
            existingPhieuNhap.setXuong(xuong);
        }

        existingPhieuNhap.getChiTietPhieuNhap().clear();
        BigDecimal tongTienPhieu = BigDecimal.ZERO; // Khởi tạo ví tiền

            if(nhapKhoRequest.getChiTietNhapKho() != null ){
                for (DNhapKhoRequest ctRequest : nhapKhoRequest.getChiTietNhapKho()) {
                    Hang hang = hangRepository.findById(ctRequest.getMaH())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy Hàng!"));
                    // Tạo chi tiết mới
                    DNhapKho dNhapKho = new DNhapKho();
                    dNhapKho.setPhieuNhap(existingPhieuNhap);
                    dNhapKho.setHang(hang);
                    dNhapKho.setSoluong(ctRequest.getSoLg());
                    dNhapKho.setGia(ctRequest.getGia());
                    // Tính tiền cộng dồn
                    BigDecimal thanhTienDong = dNhapKho.getThanhTien();
                    tongTienPhieu = tongTienPhieu.add(thanhTienDong);
                    // Thả chi tiết mới vào giỏ hàng của Phiếu
                    existingPhieuNhap.getChiTietPhieuNhap().add(dNhapKho);
                }
            }
        //  Cập nhật lại tổng tiền
        existingPhieuNhap.setTongtien(tongTienPhieu);
        // Lưu tất cả (Lưu 1 lần duy nhất)
        PhieuNhap updatedPhieuNhap = phieuNhapRepository.save(existingPhieuNhap);
        // Trả về kết quả
        return nhapKhoMapper.toNhapKhoResponse(updatedPhieuNhap);
    }

    @Transactional
    @Override
    public void deletePhieuNhap(String mapnhap) {
        // tìm phiếu nhập
        PhieuNhap phieuNhap = phieuNhapRepository.findById(mapnhap)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập với mã: " + mapnhap));
        if(phieuNhap.getTrangThai() != 0 ){
            throw new RuntimeException("Phiếu nhập đã được duyệt, không thể xóa!");
        }
        phieuNhapRepository.delete(phieuNhap);
    }
}
