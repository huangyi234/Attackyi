package com.attackyi.service;

import com.attackyi.domain.AuditLog;
import com.attackyi.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void record(String actor, String action, String resource) {
        AuditLog log = new AuditLog();
        log.setActor(actor);
        log.setAction(action);
        log.setResource(resource);
        auditLogRepository.save(log);
    }
}
