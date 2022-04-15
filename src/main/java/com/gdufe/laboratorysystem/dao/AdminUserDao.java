package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.AdminInfo;
import com.gdufe.laboratorysystem.entity.TeacherInfo;
import com.gdufe.laboratorysystem.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserDao {
    /**
     * 获取的管理用户信息
     *
     * */
    User getAdmin(@Param("username") String username);

    /**
     * 忘记密码，修改密码
     * @param
     * @return
     */
    int upPasswrodEmail(@Param("username") String username,@Param("password") String password
            ,@Param("email") String email);

    //获取密码
    String getPassword(String username);

    //根据用户名查询个人信息
    AdminInfo getAdminInfo(@Param("username") String username);

    //根据用户名查询个人账号信息
    User getUserInfo(@Param("username") String username);
    /**
     * 更新管理园账号信息
     */
    int upAdminUserInfo(User user);
    /**
     * 修改管理员用户名密码
     */
    int upPassword(@Param("username") String username,@Param("password") String password);
    ///超级管理员superUsername是登录用户
    int upAdminPasswrod(@Param("username") String username,@Param("password") String password,
                        @Param("superUsername") String superUsername);

    /**
     * 超级管理员账号查询管理列表
     */
    List<T> getAdminInfoList(@Param("username") String username , TeacherInfo teacherInfo);
}
