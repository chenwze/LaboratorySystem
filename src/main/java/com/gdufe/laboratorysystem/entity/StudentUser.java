package com.gdufe.laboratorysystem.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Date;
import java.util.Collection;

/**
 * @program: LaboratorySystem
 * @description: 学生实体类
 * @author: chen weize
 * @create: 2022-01-25 10:37
 * @version: 1.0
 */
public class StudentUser extends User {
    //用户账号
//    private String username;
//    private String name;
//    private String password;
//    //权限
//    private String role;
//    //账户状态
//    private String status;
//    //账户头像
//    private String headPortrait;
//    private String email;
//    private String createTime;
    private String studayDate;
    private String graduationDate;
    private String major;
    private String aclass;
    private char sex;
    private String birth;
    //家庭住址
    private String address;
    //学院
    private String college;
    private String tel;
    private int age;

    public String getStudayDate() {
        return studayDate;
    }

    public void setStudayDate(String studayDate) {
        this.studayDate = studayDate;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(String graduationDate) {
        this.graduationDate = graduationDate;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAclass() {
        return aclass;
    }

    public void setAclass(String aclass) {
        this.aclass = aclass;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
