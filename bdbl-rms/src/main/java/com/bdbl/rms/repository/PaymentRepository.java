package com.bdbl.rms.repository;

import com.bdbl.rms.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentNumber(String paymentNumber);

    List<Payment> findByTenantId(Long tenantId);

    List<Payment> findByRentBillId(Long rentBillId);

    boolean existsByPaymentNumber(String paymentNumber);
}
