package com.gdufe.laboratorysystem.entity;

import java.io.Serializable;

/**
 * @program: LaboratorySystem
 * @description: 预约表实体类
 * @author: chen weize
 * @create: 2022-03-12 02:02
 * @version: 1.0
 */
public class Reserve implements Serializable {
    private static final long serialVersionUID = -7532195954593898274L;
    //预约表唯一编号
    private String  id;
    //实验室唯一编号
    private int labid;
    //预约时间
    private String reserveTime;
    //预约实验的用户名
    private String username;
    //预约实验室的用户类型，学生还是老师
    private  String userType;

    //实验室信息
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

    public int getLabid() {
        return labid;
    }

    public void setLabid(int labid) {
        this.labid = labid;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "Reserver{" +
                "id=" + id +
                ", labid=" + labid +
                ", reserveTime='" + reserveTime + '\'' +
                ", username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
