package com.project.SkillHub.Exception;

public class TenantHasRecordsException extends RuntimeException {

    public TenantHasRecordsException(String message) {
        super(message);
    }
}