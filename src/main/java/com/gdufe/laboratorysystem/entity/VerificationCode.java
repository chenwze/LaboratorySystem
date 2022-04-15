package com.gdufe.laboratorysystem.entity;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create
 * @version: 1.0
 */
public class VerificationCode {
    private String id;
    private String code;
    private Long endTime;
    private Long startTime;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
