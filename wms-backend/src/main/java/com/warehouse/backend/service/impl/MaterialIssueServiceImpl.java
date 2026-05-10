package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.MaterialIssueDetailRequest;
import com.warehouse.backend.dto.request.MaterialIssueRequest;
import com.warehouse.backend.dto.response.MaterialIssueResponse;
import com.warehouse.backend.entity.danhmuc.Material;
import com.warehouse.backend.entity.danhmuc.Supplier;
import com.warehouse.backend.entity.danhmuc.Zone;
import com.warehouse.backend.entity.hethong.User;
import com.warehouse.backend.entity.nghiepvu.MaterialIssueDetail;
import com.warehouse.backend.entity.nghiepvu.OutboundMaterialIssue;
import com.warehouse.backend.mapper.MaterialIssueMapper;
import com.warehouse.backend.repository.*;
import com.warehouse.backend.service.IMaterialIssueService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialIssueServiceImpl implements IMaterialIssueService {
    private final MaterialIssueRepository materialIssueRepository;
    private final MaterialIssueMapper materialIssueMapper;
    private final MaterialRepository materialRepository;
    private final ZoneRepository zoneRepository;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;

    public MaterialIssueServiceImpl(
            MaterialIssueRepository materialIssueRepository,
            MaterialIssueMapper materialIssueMapper,
            MaterialRepository materialRepository,
            ZoneRepository zoneRepository,
            UserRepository userRepository,
            SupplierRepository supplierRepository
    ) {
        this.materialIssueRepository = materialIssueRepository;
        this.materialIssueMapper = materialIssueMapper;
        this.materialRepository = materialRepository;
        this.zoneRepository = zoneRepository;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
    }
    private OutboundMaterialIssue findMaterialIssueById(String materialIssueId) {
        return materialIssueRepository.findById(materialIssueId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu xuất nguyên liệu với ID: " + materialIssueId));
    }

    @Override
    public String generateNextMaterialIssueId() {
        String maxId = materialIssueRepository.findMaxMaterialIssueId();
        if (maxId == null) {
            return "PXNL001";
        }
        // Cắt chuỗi: substring(4) để lấy phần số từ vị trí 4 trở đi
        int nextNumber = Integer.parseInt(maxId.substring(4)) + 1;
        return String.format("PXNL%03d", nextNumber);
    }

    @Override
    public List<MaterialIssueResponse> getAllMaterialIssues() {
        return materialIssueRepository.findAll()
                .stream()
                .map(materialIssueMapper::toMaterialIssueResponse)
                .toList();
    }

    @Override
    public MaterialIssueResponse getMaterialIssueById(String materialIssueId) {
        OutboundMaterialIssue materialIssue = findMaterialIssueById(materialIssueId);
        return materialIssueMapper.toMaterialIssueResponse(materialIssue);
    }


    @Override
    @Transactional
    public MaterialIssueResponse saveMaterialIssue(MaterialIssueRequest materialIssueRequest) {
        // 1. Kiểm tra danh sách chi tiết không được rỗng
        if (materialIssueRequest.getMaterialIssueDetails() == null
                || materialIssueRequest.getMaterialIssueDetails().isEmpty()) {
            throw new RuntimeException("Chi tiết phiếu xuất nguyên liệu không được để trống");
        }

        // 2. Tạo entity từ request (mapper sẽ ignore user, supplier, materialIssueDetails)
        OutboundMaterialIssue materialIssue = materialIssueMapper.toOutboundMaterialIssueEntity(materialIssueRequest);

        // 3. Set ID tự sinh, Date hiện tại, Status = 0 (Nháp)
        materialIssue.setMaterialIssueId(generateNextMaterialIssueId());
        materialIssue.setMaterialIssueDate(LocalDate.now());
        materialIssue.setStatus(0);

        // 4. Tìm User (Người lập)
        User user = userRepository.findById(materialIssueRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + materialIssueRequest.getUserId()));
        materialIssue.setUser(user);

        // 5. Tìm Supplier (Đại diện Xưởng)
        if (materialIssueRequest.getSupplierId() != null && !materialIssueRequest.getSupplierId().isEmpty()) {
            Supplier supplier = supplierRepository.findById(materialIssueRequest.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy xưởng với ID: " + materialIssueRequest.getSupplierId()));
            materialIssue.setSupplier(supplier);
        }

        // 6. Xử lý chi tiết nguyên liệu
        List<MaterialIssueDetail> materialIssueDetails = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (MaterialIssueDetailRequest detailRequest : materialIssueRequest.getMaterialIssueDetails()) {
            // Tìm Material
            Material material = materialRepository.findById(detailRequest.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyên liệu với ID: " + detailRequest.getMaterialId()));

            // Tìm Zone
            Zone zone = null;
            if (detailRequest.getZoneId() != null && !detailRequest.getZoneId().isEmpty()) {
                zone = zoneRepository.findById(detailRequest.getZoneId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực với ID: " + detailRequest.getZoneId()));
            } else {
                // Nếu không chỉ định zone, lấy zone mặc định của Material
                zone = material.getZone();
                if (zone == null) {
                    throw new RuntimeException("Nguyên liệu không có khu vực mặc định. Vui lòng chỉ định khu vực");
                }
            }

            // BẮT BUỘC: Kiểm tra zone.getZoneType() != 0
            // zoneType = 1: Nguyên liệu, zoneType = 2: Thành phẩm
            if (zone.getZoneType() != null && zone.getZoneType() != 1) {
                throw new RuntimeException("Không thể xuất nguyên liệu ở khu vực loại " + zone.getZoneType() + ". " +
                        "Khu vực này dành cho thành phẩm, vui lòng chọn khu vực nguyên liệu");
            }
            int quantityToIssue = detailRequest.getQuantity();
            if (quantityToIssue > material.getQuantity()) {
                throw new RuntimeException("Không thể tạo phiếu! Nguyên liệu '" + material.getMaterialName() +
                        "' chỉ còn " + material.getQuantity() + " trong kho, nhưng bạn muốn xuất " + quantityToIssue);
            }

            // Tạo dòng chi tiết
            MaterialIssueDetail detail = new MaterialIssueDetail();
            detail.setOutboundMaterialIssue(materialIssue);
            detail.setMaterial(material);
            detail.setZone(zone);
            detail.setQuantity(detailRequest.getQuantity());
            detail.setPrice(detailRequest.getPrice());

            // Tính SubTotal và cộng vào TotalAmount
            BigDecimal subTotal = detail.getSubTotal();
            totalAmount = totalAmount.add(subTotal);

            // Thêm chi tiết vào danh sách
            materialIssueDetails.add(detail);
        }

        // 7. Gán danh sách chi tiết và tổng tiền vào phiếu
        materialIssue.setMaterialIssueDetails(materialIssueDetails);
        materialIssue.setTotalAmount(totalAmount);

        // 8. Lưu phiếu
        OutboundMaterialIssue savedMaterialIssue = materialIssueRepository.save(materialIssue);

        // 9. Trả về response
        return materialIssueMapper.toMaterialIssueResponse(savedMaterialIssue);
    }


    @Override
    @Transactional
    public MaterialIssueResponse approveMaterialIssue(String materialIssueId) {
        // 1. Tìm phiếu
        OutboundMaterialIssue materialIssue = findMaterialIssueById(materialIssueId);

        // 2. Kiểm tra trạng thái: chỉ được duyệt khi status == 0
        if (materialIssue.getStatus() != 0) {
            throw new RuntimeException("Chỉ có thể duyệt phiếu ở trạng thái Nháp (status = 0). " +
                    "Phiếu này đã được duyệt hoặc đã hủy");
        }

        // 3. Lặp qua chi tiết để kiểm tra và trừ tồn kho
        for (MaterialIssueDetail detail : materialIssue.getMaterialIssueDetails()) {
            Material material = detail.getMaterial();
            Zone zone = detail.getZone();// Lấy khu vực của sản phẩm này
            int quantityToDeduct = detail.getQuantity();


            // Kiểm tra tồn kho
            if (material.getQuantity() < detail.getQuantity()) {
                throw new RuntimeException("Không đủ hàng trong kho cho nguyên liệu: " + material.getMaterialName() +
                        ". Tồn kho: " + material.getQuantity() + ", yêu cầu: " + detail.getQuantity());
            }
            // Trừ tồn kho
            material.setQuantity(material.getQuantity() - quantityToDeduct);
            materialRepository.save(material);

            // GIẢI PHÓNG KHÔNG GIAN CHO KHU VỰC (Zone)
            if (zone != null) {
                int currentLoad = zone.getCurrentLoad() != null ? zone.getCurrentLoad() : 0;
                int newCurrentLoad = currentLoad - quantityToDeduct;

                // Đảm bảo tải trọng không bị âm (đề phòng dữ liệu cũ bị sai lệch)
                if (newCurrentLoad < 0) {
                    newCurrentLoad = 0;
                }
                zone.setCurrentLoad(newCurrentLoad);
                zoneRepository.save(zone); // Lưu Zone xuống DB
            }
        }

        // 4. Cập nhật trạng thái phiếu thành 1 (Đã xuất)
        materialIssue.setStatus(1);

        // 5. Lưu phiếu
        OutboundMaterialIssue approvedMaterialIssue = materialIssueRepository.save(materialIssue);

        // 6. Trả về response
        return materialIssueMapper.toMaterialIssueResponse(approvedMaterialIssue);
    }

    @Override
    @Transactional
    public MaterialIssueResponse updateMaterialIssue(String materialIssueId, MaterialIssueRequest materialIssueRequest) {
        if (materialIssueRequest.getMaterialIssueDetails() == null
                || materialIssueRequest.getMaterialIssueDetails().isEmpty()) {
            throw new RuntimeException("Chi tiết phiếu xuất nguyên liệu không được để trống!");
        }
        // 1. Tìm phiếu cũ
        OutboundMaterialIssue existingMaterialIssue = findMaterialIssueById(materialIssueId);

        // 2. Kiểm tra trạng thái: chỉ được update khi status == 0
        if (existingMaterialIssue.getStatus() != 0) {
            throw new RuntimeException("Chỉ có thể cập nhật phiếu ở trạng thái Nháp (status = 0). " +
                    "Phiếu này đã được duyệt hoặc đã hủy");
        }

        // 3. Cập nhật thông tin cơ bản từ request
        materialIssueMapper.updateFromRequest(materialIssueRequest, existingMaterialIssue);

        // 4. Cập nhật User nếu khác
        if (materialIssueRequest.getUserId() != null) {
            User user = userRepository.findById(materialIssueRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + materialIssueRequest.getUserId()));
            existingMaterialIssue.setUser(user);
        }

        // 5. Cập nhật Supplier nếu có
        if (materialIssueRequest.getSupplierId() != null && !materialIssueRequest.getSupplierId().isEmpty()) {
            Supplier supplier = supplierRepository.findById(materialIssueRequest.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy xưởng với ID: " + materialIssueRequest.getSupplierId()));
            existingMaterialIssue.setSupplier(supplier);
        }

        // 6. Clear chi tiết cũ
        existingMaterialIssue.getMaterialIssueDetails().clear();

        // 7. Xử lý chi tiết mới
        BigDecimal totalAmount = BigDecimal.ZERO;
            for (MaterialIssueDetailRequest detailRequest : materialIssueRequest.getMaterialIssueDetails()) {
                // Tìm Material
                Material material = materialRepository.findById(detailRequest.getMaterialId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyên liệu với ID: " + detailRequest.getMaterialId()));

                // Tìm Zone
                Zone zone = null;
                if (detailRequest.getZoneId() != null && !detailRequest.getZoneId().isEmpty()) {
                    zone = zoneRepository.findById(detailRequest.getZoneId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực với ID: " + detailRequest.getZoneId()));
                } else {
                    zone = material.getZone();
                    if (zone == null) {
                        throw new RuntimeException("Nguyên liệu không có khu vực mặc định. Vui lòng chỉ định khu vực");
                    }
                }

                // BẮT BUỘC: Kiểm tra zone.getZoneType() != 0
                if (zone.getZoneType() != null && zone.getZoneType() != 1) {
                    throw new RuntimeException("Không thể xuất nguyên liệu ở khu vực loại " + zone.getZoneType() + ". " +
                            "Khu vực này dành cho thành phẩm, vui lòng chọn khu vực nguyên liệu");
                }
                int quantityToIssue = detailRequest.getQuantity();
                if (quantityToIssue > material.getQuantity()) {
                    throw new RuntimeException("Không thể cập nhật phiếu! Nguyên liệu '" + material.getMaterialName() +
                            "' chỉ còn " + material.getQuantity() + " trong kho, nhưng bạn muốn xuất " + quantityToIssue);
                }
                // Tạo dòng chi tiết
                MaterialIssueDetail detail = new MaterialIssueDetail();
                detail.setOutboundMaterialIssue(existingMaterialIssue);
                detail.setMaterial(material);
                detail.setZone(zone);
                detail.setQuantity(detailRequest.getQuantity());
                detail.setPrice(detailRequest.getPrice());

                // Tính SubTotal và cộng vào TotalAmount
                BigDecimal subTotal = detail.getSubTotal();
                totalAmount = totalAmount.add(subTotal);

                // Thêm chi tiết vào danh sách
                existingMaterialIssue.getMaterialIssueDetails().add(detail);
            }

        // 8. Cập nhật tổng tiền
        existingMaterialIssue.setTotalAmount(totalAmount);

        // 9. Lưu phiếu
        OutboundMaterialIssue updatedMaterialIssue = materialIssueRepository.save(existingMaterialIssue);

        // 10. Trả về response
        return materialIssueMapper.toMaterialIssueResponse(updatedMaterialIssue);
    }

    @Override
    @Transactional
    public MaterialIssueResponse cancelMaterialIssue(String materialIssueId) {
        // 1. Tìm phiếu
        OutboundMaterialIssue materialIssue = findMaterialIssueById(materialIssueId);

        // 2. Kiểm tra trạng thái: chỉ được hủy khi status == 0
        if (materialIssue.getStatus() != 0) {
            throw new RuntimeException("Chỉ có thể hủy phiếu ở trạng thái Nháp (status = 0). " +
                    "Phiếu này đã được duyệt hoặc đã hủy");
        }

        // 3. Set status = -1 (Đã hủy)
        materialIssue.setStatus(-1);

        // 4. Lưu phiếu
        OutboundMaterialIssue cancelledMaterialIssue = materialIssueRepository.save(materialIssue);

        // 5. Trả về response
        return materialIssueMapper.toMaterialIssueResponse(cancelledMaterialIssue);
    }
}

