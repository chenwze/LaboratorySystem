package com.gdufe.laboratorysystem.entity;

import java.io.Serializable;

/**
 * @program: LaboratorySystem
 * @description: 管理员信息
 * @author: chen weize
 * @create: 2022-02-13 19:24
 * @version: 1.0
 */
public class AdminUser extends User {
    private static final long serialVersionUID = -6470903421414880872L;
    private String sex;
    private String tel;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
