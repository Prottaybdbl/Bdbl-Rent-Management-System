package com.bdbl.rms.repository;

import com.bdbl.rms.entity.TenantContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantContactRepository extends JpaRepository<TenantContact, Long> {

    List<TenantContact> findByTenantId(Long tenantId);

    // Custom query to set all previous primary contacts to false
    // Implementation usually in service by querying and updating, or via @Query
}
