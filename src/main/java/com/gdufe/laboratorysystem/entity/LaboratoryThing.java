package com.gdufe.laboratorysystem.entity;

/**
 * @program: LaboratorySystem
 * @description: 实验室物品
 * @author: chen weize
 * @create: 2022-03-31 00:51
 * @version: 1.0
 */
public class LaboratoryThing {
    private String id;
    private String labid;
    private String type;
    private String way;
    private String name;
    private String introduce;
    private String time;
    private String buildingName;
    private LaboratoryInfo laboratoryInfo;

    public LaboratoryInfo getLaboratoryInfo() {
        return laboratoryInfo;
    }

    public void setLaboratoryInfo(LaboratoryInfo laboratoryInfo) {
        this.laboratoryInfo = laboratoryInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabid() {
        return labid;
    }

    public void setLabid(String labid) {
        this.labid = labid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}