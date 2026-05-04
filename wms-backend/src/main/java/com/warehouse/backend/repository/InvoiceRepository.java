package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    @Query("SELECT MAX(inv.invoiceId) FROM Invoice inv ")
    String findMaxInvoiceId();
}
