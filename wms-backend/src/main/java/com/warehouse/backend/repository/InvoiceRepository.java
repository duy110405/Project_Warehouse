package com.warehouse.backend.repository;

import com.warehouse.backend.entity.nghiepvu.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    @Query("SELECT MAX(inv.invoiceId) FROM Invoice inv ")
    String findMaxInvoiceId();

    @Query("SELECT i FROM Invoice i WHERE "+
     "(:status IS NULL OR i.status = :status) AND "+
      "(:search IS NULL OR i.invoiceId LIKE %:search%) AND " +
     "(:customerId IS NULL OR i.customer.customerId = :customerId)")
    List<Invoice> searchInvoice (@Param("status") Integer status,
                                 @Param("search") String search,
                                 @Param("customerId") String customerId);
}
