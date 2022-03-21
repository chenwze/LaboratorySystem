package com.gdufe.laboratorysystem.entity;

import java.io.Serializable;

/**
 * @program: LaboratorySystem
 * @description: 教师个人信息
 * @author: chen weize
 * @create: 2022-02-13 19:24
 * @version: 1.0
 */
public class TeacherInfo extends BasicInformation implements Serializable {
    private static final long serialVersionUID = -3772807204912158417L;

    private String entryDate;
    private String resignationDate;
    private String major;
    private String position;

    @Override
    public String toString() {
        return "TeacherInfo{" +
                "entryDate='" + entryDate + '\'' +
                ", resignationDate='" + resignationDate + '\'' +
                ", major='" + major + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(String resignationDate) {
        this.resignationDate = resignationDate;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
