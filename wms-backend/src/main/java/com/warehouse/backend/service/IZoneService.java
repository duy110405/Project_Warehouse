package com.warehouse.backend.service;

import com.warehouse.backend.dto.request.ZoneRequest;
import com.warehouse.backend.dto.response.ZoneRespone;

import java.util.List;

public interface IZoneService {
    ZoneRespone getZoneById(String zoneId);
     ZoneRespone saveZone(ZoneRequest zoneRequest);
     ZoneRespone updateZone(String zoneId, ZoneRequest zoneRequest);
     void deleteZone(String zoneId);
     String generateNextZoneId();
     List<ZoneRespone> getAllZone();
}
