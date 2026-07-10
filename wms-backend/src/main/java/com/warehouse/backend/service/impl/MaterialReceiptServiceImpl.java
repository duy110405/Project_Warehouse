package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.MaterialReceiptDetailRequest;
import com.warehouse.backend.dto.request.MaterialReceiptRequest;
import com.warehouse.backend.dto.response.MaterialReceiptResponse;
import com.warehouse.backend.entity.danhmuc.*;
import com.warehouse.backend.entity.hethong.User;
import com.warehouse.backend.entity.nghiepvu.InboundMaterialReceipt;
import com.warehouse.backend.entity.nghiepvu.MaterialReceiptDetail;
import com.warehouse.backend.mapper.MaterialReceiptMapper;
import com.warehouse.backend.repository.*;
import com.warehouse.backend.service.IMaterialReceiptService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialReceiptServiceImpl implements IMaterialReceiptService {
    private final MaterialReceiptMapper materialReceiptMapper;
    private final MaterialReceiptRepository materialReceiptRepository;
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    private final VendorRepository vendorRepository;
    private final ZoneRepository zoneRepository;

    public MaterialReceiptServiceImpl(MaterialReceiptMapper materialReceiptMapper, MaterialReceiptRepository materialReceiptRepository, UserRepository userRepository, MaterialRepository materialRepository, VendorRepository vendorRepository, ZoneRepository zoneRepository) {
        this.materialReceiptMapper = materialReceiptMapper;
        this.materialReceiptRepository = materialReceiptRepository;
        this.userRepository = userRepository;
        this.materialRepository = materialRepository;
        this.vendorRepository = vendorRepository;
        this.zoneRepository = zoneRepository;
    }

    public InboundMaterialReceipt findMaterialReceiptById(String materialReceiptId) {
        return materialReceiptRepository.findById(materialReceiptId)
                .orElseThrow(() -> new RuntimeException("không tìm thấy phiếu nhập nguyên liệu"));
    }

    @Override
    public String generateNextMaterialReceiptId() {
        String maxId = materialReceiptRepository.findMaxMaterialReceiptId();
        if (maxId == null) return "PNNL001";
        int nextNumber = Integer.parseInt(maxId.substring(4)) + 1;
        return String.format("PNNL%03d", nextNumber);
    }

     @Override
    public Page<MaterialReceiptResponse> searchReceipts(Integer status, String search, String vendorId , Pageable pageable) {
       Page<InboundMaterialReceipt> receiptsMaterialPage = materialReceiptRepository.searchInboundMaterialReceipts(status, search, vendorId , pageable);
        return receiptsMaterialPage.map(materialReceiptMapper::toMaterialReceiptResponse);
    }

    @Override
    public MaterialReceiptResponse getMaterialReceiptById(String materialReceiptId) {
        InboundMaterialReceipt materialReceipt = findMaterialReceiptById(materialReceiptId);
        return materialReceiptMapper.toMaterialReceiptResponse(materialReceipt);
    }

    @Override
    @Transactional
    public MaterialReceiptResponse saveMaterialReceipt(MaterialReceiptRequest materialReceiptRequest) {
        if (materialReceiptRequest.getMaterialReceiptDetails() == null || materialReceiptRequest.getMaterialReceiptDetails().isEmpty()) {
            throw new RuntimeException("Phiếu nhập nguyên liệu phải có ít nhất một nguyên liệu ");
        }

        InboundMaterialReceipt inboundMaterialReceipt = materialReceiptMapper.toInboundMaterialReceiptEntity(materialReceiptRequest);
        inboundMaterialReceipt.setMaterialReceiptId(generateNextMaterialReceiptId());
        inboundMaterialReceipt.setMaterialReceiptDate(LocalDate.now());

        //Tìm User từ Database và gán vào Phiếu
        User user = userRepository.findByUsername(materialReceiptRequest.getCreateBy())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + materialReceiptRequest.getCreateBy()));
        inboundMaterialReceipt.setUser(user);

        // Tìm Xưởng và gán vào Phiếu
        Vendor vendor = vendorRepository.findById(materialReceiptRequest.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found!"));
        inboundMaterialReceipt.setVendor(vendor);

        // xử lý Chi tiết nhập kho
        List<MaterialReceiptDetail> materialReceiptDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (MaterialReceiptDetailRequest materialReceiptDetailRequest : materialReceiptRequest.getMaterialReceiptDetails()) {
            Material material = materialRepository.findById(materialReceiptDetailRequest.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Nguyên liệu không tồn tại!"));

            // TÌM ZONE LINH HOẠT
            Zone zone = null;
            if (materialReceiptDetailRequest.getZoneId() != null) {
                zone = zoneRepository.findById(materialReceiptDetailRequest.getZoneId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Khu vực!"));
            } else {
                zone = material.getZone();
            }
            if (zone.getZoneType() != 1) {
                throw new RuntimeException("Lỗi: Không thể cất Nguyên Liệu vào Khu Thành Phẩm (" + zone.getZoneName() + ")!");
            }

            if (zone != null && zone.getCapacity() != null) {
                int currentLoad = zone.getCurrentLoad();
                int amountToAdd = materialReceiptDetailRequest.getQuantity();

                if (currentLoad + amountToAdd > zone.getCapacity()) {
                    throw new RuntimeException("Không thể lưu phiếu! Khu vực '" + zone.getZoneName() +
                            "' sẽ bị quá tải. Sức chứa: " + zone.getCapacity() +
                            ", Đang có: " + currentLoad +
                            ", Chuẩn bị nhập: " + amountToAdd);
                }
            }
            // Tạo dòng chi tiết
            MaterialReceiptDetail materialReceiptDetail = new MaterialReceiptDetail();
            materialReceiptDetail.setInboundMaterialReceipt(inboundMaterialReceipt);
            materialReceiptDetail.setMaterial(material);
            materialReceiptDetail.setQuantity(materialReceiptDetailRequest.getQuantity());
            materialReceiptDetail.setPrice(materialReceiptDetailRequest.getPrice());
            materialReceiptDetail.setZone(zone);

            // tính tổng tiền
            BigDecimal SubTotal = materialReceiptDetail.getSubTotal();
            // Cộng dồn vào Tổng tiền của tờ phiếu
            totalAmount = totalAmount.add(SubTotal);
            // Bỏ dòng này vào giỏ
            materialReceiptDetails.add(materialReceiptDetail);
        }
        inboundMaterialReceipt.setTotalAmount(totalAmount);
        inboundMaterialReceipt.setMaterialReceiptDetails(materialReceiptDetails);
        InboundMaterialReceipt savedInboundReceipt = materialReceiptRepository.save(inboundMaterialReceipt);
        return materialReceiptMapper.toMaterialReceiptResponse(savedInboundReceipt);
    }

    @Override
    @Transactional
    public MaterialReceiptResponse approveMaterialReceipt(String materialReceiptId) {
        // tìm phiếu nhập
        InboundMaterialReceipt inboundMaterialReceipt = materialReceiptRepository.findById(materialReceiptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập nguyên liệu với mã: " + materialReceiptId));
        if(inboundMaterialReceipt.getStatus() != 0 ){
            throw new RuntimeException("Phiếu nhập nguyên liệu đã được duyệt hoặc đã hủy, không thể duyệt lại!");
        }

        inboundMaterialReceipt.setStatus(1); // Cập nhật trạng thái thành "Đã duyệt"
        for (MaterialReceiptDetail detail : inboundMaterialReceipt.getMaterialReceiptDetails()) {
            Material material = detail.getMaterial(); // Lấy nguyên liệu của dòng chi tiết này
            Zone zone = detail.getZone(); // Lấy khu vực của sản phẩm này

            if (zone != null && zone.getCapacity() != null) {
                // Đếm xem khu này đang chứa bao nhiêu hàng rồi
                int currentLoad = zone.getCurrentLoad();
                int amountToAdd = detail.getQuantity();

                // Nếu Hàng đang có + Hàng chuẩn bị nhập > Sức chứa -> BÁO LỖI CHẶN LẠI NGAY!
                if (currentLoad + amountToAdd > zone.getCapacity()) {
                    throw new RuntimeException("Không thể duyệt phiếu! Khu vực " + zone.getZoneName() +
                            " đã đầy. Sức chứa: " + zone.getCapacity() +
                            ", Đang có: " + currentLoad +
                            ", Chuẩn bị nhập thêm: " + amountToAdd);
                }
            }
            // Cộng dồn kho
            int newQuantity = material.getQuantity() + detail.getQuantity();
            material.setQuantity(newQuantity);
            System.out.println("Cập nhật Nguyên liệu: " + material.getMaterialName());
            System.out.println("Số cũ: " + (newQuantity - detail.getQuantity()) + " | Số mới: " + material.getQuantity());
            int newCurrentLoad = zone.getCurrentLoad() + detail.getQuantity();
            zone.setCurrentLoad(newCurrentLoad);
            // Cập nhật Hàng xuống DB
            materialRepository.save(material);
            zoneRepository.save(zone);
        }
        InboundMaterialReceipt savedInboundReceipt = materialReceiptRepository.save(inboundMaterialReceipt);
        return materialReceiptMapper.toMaterialReceiptResponse(savedInboundReceipt);
    }

    @Override
    @Transactional
    public MaterialReceiptResponse updateMaterialReceipt(String materialReceiptId , MaterialReceiptRequest materialReceiptRequest){
        // Tìm phiếu cũ trong db và phiếu phải ở trạng thái chưa duyệt
        InboundMaterialReceipt existingMaterialReceipt = materialReceiptRepository.findById(materialReceiptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập nguyên liệu với mã: " + materialReceiptId));
        if(existingMaterialReceipt.getStatus() != 0 ) {
            throw new RuntimeException("Phiếu nhập nguyên liệu đã được duyệt hoặc đã hủy, không thể cập nhật!");
        }
        materialReceiptMapper.updateFromRequest(materialReceiptRequest, existingMaterialReceipt);
        // Cập nhật lại Xưởng nếu FE có gửi Xưởng mới
        if(materialReceiptRequest.getVendorId() != null) {
            Vendor vendor = vendorRepository.findById(materialReceiptRequest.getVendorId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy NCC!"));
            existingMaterialReceipt.setVendor(vendor);
        }

        existingMaterialReceipt.getMaterialReceiptDetails().clear();
        BigDecimal totalAmount = BigDecimal.ZERO; // Khởi tạo ví tiền

        if(materialReceiptRequest.getMaterialReceiptDetails() != null ){
            for (MaterialReceiptDetailRequest detailRequest : materialReceiptRequest.getMaterialReceiptDetails()) {
                Material material = materialRepository.findById(detailRequest.getMaterialId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyên liệu!"));

                Zone zone = null;
                if (detailRequest.getZoneId() != null) {
                    zone = zoneRepository.findById(detailRequest.getZoneId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy Khu vực!"));
                } else {
                    zone =  material.getZone();
                }
                if (zone.getZoneType() != 1) {
                    throw new RuntimeException("Lỗi: Không thể cất Nguyên Liệu vào Khu Thành Phẩm (" + zone.getZoneName() + ")!");
                }

                // --- LOGIC FAIL-FAST: Chặn ngay từ lúc sửa nháp nếu thấy số lượng vô lý ---
                if (zone != null && zone.getCapacity() != null) {
                    int currentLoad = zone.getCurrentLoad();
                    int amountToAdd = detailRequest.getQuantity();

                    if (currentLoad + amountToAdd > zone.getCapacity()) {
                        throw new RuntimeException("Cannot save receipt! Zone '" + zone.getZoneName() +
                                "' will be overloaded. Capacity: " + zone.getCapacity() +
                                ", Current load: " + currentLoad +
                                ", Attempting to add: " + amountToAdd);
                    }
                }
                // Tạo chi tiết mới
                MaterialReceiptDetail receiptDetail = new MaterialReceiptDetail();
                receiptDetail.setInboundMaterialReceipt(existingMaterialReceipt);
                receiptDetail.setMaterial(material);
                receiptDetail.setQuantity(detailRequest.getQuantity());
                receiptDetail.setPrice(detailRequest.getPrice());
                receiptDetail.setZone(zone);
                // Tính tiền cộng dồn
                BigDecimal subTotal = receiptDetail.getSubTotal();
                totalAmount = totalAmount.add(subTotal);
                // Thả chi tiết mới vào giỏ hàng của Phiếu
                existingMaterialReceipt.getMaterialReceiptDetails().add(receiptDetail);
            }
        }
        //  Cập nhật lại tổng tiền
        existingMaterialReceipt.setTotalAmount(totalAmount);
        // Lưu tất cả (Lưu 1 lần duy nhất)
         InboundMaterialReceipt updatedInboundReceipt = materialReceiptRepository.save(existingMaterialReceipt);
        // Trả về kết quả
        return materialReceiptMapper.toMaterialReceiptResponse(updatedInboundReceipt);
    }

    @Transactional
    @Override
    public MaterialReceiptResponse cancelMaterialReceipt(String materialReceiptId) {
        // tìm phiếu nhập
        InboundMaterialReceipt inboundReceipt = findMaterialReceiptById(materialReceiptId);
        if(inboundReceipt.getStatus() != 0 ){
            throw new RuntimeException("Phiếu nhập đã được duyệt, không thể xóa!");
        }
        inboundReceipt.setStatus(-1);
       InboundMaterialReceipt canceledInboundReceipt = materialReceiptRepository.save(inboundReceipt);
        return materialReceiptMapper.toMaterialReceiptResponse(canceledInboundReceipt);
    }

}

