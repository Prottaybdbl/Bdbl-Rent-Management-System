package com.bdbl.rms.repository;

import com.bdbl.rms.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserId(Long userId);

    List<AuditLog> findByActionType(String actionType);

    List<AuditLog> findByTableName(String tableName);
}
