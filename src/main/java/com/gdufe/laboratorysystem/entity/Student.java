package com.gdufe.laboratorysystem.entity;

import org.springframework.security.core.GrantedAuthority;
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
public class Student extends BasicInformation implements Serializable,UserDetails{
    private static final long serialVersionUID = -8539697792547976377L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
