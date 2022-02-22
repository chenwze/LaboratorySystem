package com.gdufe.laboratorysystem.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * @program: LaboratorySystem
 * @description: 个人基础信息
 * @author: chen weize
 * @create: 2022-02-13 18:56
 * @version: 1.0
 */
public class BasicInformation {
    //登录账号
    private String userid;
    //用户名称
    private String username;
    private String password;
    private char sex;
    private String tel;
    //权限
    private String role;
    //家庭住址
    private String address;
    //学院
    private String college;
    //账户状态
    private String status;
    //账户头像
    private String headPortrait;
    private Date birth;
    private int age;
    //是否锁住
    private boolean locked;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "BasicInformation{" +
                "userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", tel='" + tel + '\'' +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                ", college='" + college + '\'' +
                ", status='" + status + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", birth=" + birth +
                ", age=" + age +
                ", locked=" + locked +
                '}';
    }

}
