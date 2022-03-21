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
public class StudentInfo extends BasicInformation implements Serializable,UserDetails{
    private static final long serialVersionUID = -8539697792547976377L;
    private String studayDate;
    private String graduationDate;
    private String major;
    private String aclass;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //以逗号分割,
        return AuthorityUtils.commaSeparatedStringToAuthorityList("student,ddd");
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "studayDate='" + studayDate + '\'' +
                ", graduationDate='" + graduationDate + '\'' +
                ", major='" + major + '\'' +
                ", aclass='" + aclass + '\'' +
                '}';
    }

    public String getStudayDate() {
        return studayDate;
    }

    public void setStudayData(String studayDate) {
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
}
