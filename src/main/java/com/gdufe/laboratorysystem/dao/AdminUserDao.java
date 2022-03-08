package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.AdminInfo;
import com.gdufe.laboratorysystem.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserDao {
    User getAdmin(@Param("username") String username);

    //根据用户名查询个人信息
    AdminInfo getStudentInfo(@Param("username") String username);

    //根据用户名查询个人账号信息
    User getUserInfo(@Param("username") String username);
}
