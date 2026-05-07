package com.warehouse.backend.service.impl;

import com.warehouse.backend.dto.request.ZoneRequest;
import com.warehouse.backend.dto.response.ZoneRespone;
import com.warehouse.backend.entity.danhmuc.Zone;
import com.warehouse.backend.mapper.ZoneMapper;
import com.warehouse.backend.repository.ZoneRepository;
import com.warehouse.backend.service.IZoneService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneImpl implements IZoneService {
    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;

    public ZoneImpl(ZoneRepository zoneRepository, ZoneMapper zoneMapper) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
    }

    public Zone findZoneById(String zoneId){
        return zoneRepository.findById(zoneId)
                .orElseThrow(()-> new RuntimeException("không tìm thấy khu mã " + zoneId));
    }
    @Override
    public String generateNextZoneId(){
        String maxId = zoneRepository.findMaxZoneId();
        if (maxId == null) return "K001";
        int nextNumber = Integer.parseInt(maxId.substring(1)) + 1;
        return String.format("K%03d", nextNumber);
    }

    @Override
    public List<ZoneRespone> getAllZone(){
        return zoneRepository.findAll().stream().map(zoneMapper::toZoneRespone).toList();
    }

    @Override
    public ZoneRespone getZoneById(String zoneId){
        Zone zone = findZoneById(zoneId);
        return zoneMapper.toZoneRespone(zone);
    }

    @Override
    @Transactional
    public ZoneRespone saveZone (ZoneRequest zoneRequest){
        Zone zone = zoneMapper.toZoneEntity(zoneRequest);
        zone.setZoneId(generateNextZoneId());
        Zone savedZone = zoneRepository.save(zone);
        return zoneMapper.toZoneRespone(savedZone);
    }

    @Override
    @Transactional
    public ZoneRespone updateZone (String zoneId , ZoneRequest zoneRequest){
        Zone existingZone = findZoneById(zoneId);
        zoneMapper.updateFromRequest(zoneRequest, existingZone);
        Zone updatedZone = zoneRepository.save(existingZone);
        return zoneMapper.toZoneRespone(updatedZone);
    }

    @Override
    public void deleteZone(String zoneId){
        Zone zone = findZoneById(zoneId);
        Integer currentLoad = zoneRepository.getCurrentLoadOfZone(zoneId);
        if(currentLoad != null && currentLoad > 0){
            throw new RuntimeException("Cannot delete zone " + zoneId + " because it currently contains " + currentLoad + " items!");
        }

        zoneRepository.delete(zone);
    }

}
