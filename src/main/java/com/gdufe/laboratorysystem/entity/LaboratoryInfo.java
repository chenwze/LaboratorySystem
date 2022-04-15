package com.gdufe.laboratorysystem.entity;

import java.io.Serializable;

/**
 * @program: LaboratorySystem
 * @description: 实验室信息实体类
 * @author: chen weize
 * @create: 2022-03-08 10:31
 * @version: 1.0
 */
public class LaboratoryInfo implements Serializable {
    private String labid;
    //实验楼名称
    private String buildingName;
    //容纳数
    private int capacity;
    //实验室类别
    private String category;
    //实验室描述
    private String describe;
    //实验室状态
    private String status;
    private String roomNumber;

    @Override
    public String toString() {
        return "LaboratoryInfo{" +
                "labid=" + labid +
                ", buildingName='" + buildingName + '\'' +
                ", capacity=" + capacity +
                ", category='" + category + '\'' +
                ", describe='" + describe + '\'' +
                ", status='" + status + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getLabid() {
        return labid;
    }

    public void setLabid(String labid) {
        this.labid = labid;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
