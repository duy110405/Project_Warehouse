package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.PhieuDat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuDatRepository extends JpaRepository<PhieuDat , String> {
    @Query("SELECT MAX(pd.mapdat) FROM PhieuDat pd")
    String findMaxMaPD();
}
