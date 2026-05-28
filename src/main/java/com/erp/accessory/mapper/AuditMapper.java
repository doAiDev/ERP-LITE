package com.erp.accessory.mapper;

import com.erp.accessory.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditMapper {
    void insertAuditLog(AuditLog auditLog);
}
