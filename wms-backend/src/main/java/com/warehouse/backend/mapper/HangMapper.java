package com.warehouse.backend.mapper;

import com.warehouse.backend.dto.request.HangRequest;
import com.warehouse.backend.dto.response.HangResponse;
import com.warehouse.backend.dto.response.NguyenLieuResponse;
import com.warehouse.backend.entity.danhmuc.Hang;
import com.warehouse.backend.entity.danhmuc.NL_H;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HangMapper {

    // Chuyển entity sang response
    @Mapping(target = "danhSachNguyenLieu", source = "chiTietNguyenLieu")
    // Móc từ bảng LoaiHang ra mã và tên để ném vào Response
    @Mapping(target = "maloai", source = "loaiHang.malh")
    @Mapping(target = "tenLoaiHang", source = "loaiHang.tenlh")
    HangResponse toHangResponse(Hang hang);

    // Chuyển Request -> Entity (Bỏ qua danhSachNguyenLieu vì mình lưu bằng tay ở Service)
    @Mapping(target = "chiTietNguyenLieu", ignore = true)
    @Mapping(target = "loaiHang" , ignore = true )
    Hang toHangEntity(HangRequest request);

    // Hàm update
    @Mapping(target = "chiTietNguyenLieu", ignore = true)
    @Mapping(target = "loaiHang", ignore = true)
    void updateHangFromRequset (HangRequest hangRequest , @MappingTarget Hang hang);

    // Dạy MapStruct cách biến 1 dòng NL_H thành 1 dòng NguyenLieuRponse
    default NguyenLieuResponse mapNLHToDTO(NL_H nlh) {
        if (nlh == null || nlh.getNguyenLieu() == null) {
            return null;
        }
        NguyenLieuResponse dto = new NguyenLieuResponse();
        dto.setManl(nlh.getNguyenLieu().getManl());
        dto.setTennl(nlh.getNguyenLieu().getTennl());
        return dto;
    }
}
