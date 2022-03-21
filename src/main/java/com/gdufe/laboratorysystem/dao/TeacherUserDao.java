package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.TeacherInfo;
import com.gdufe.laboratorysystem.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TeacherUserDao {
    /**
     * 根据老师用户名查询
     */
    User getTeacher(@Param("username") String username);


    //根据用户名查询个人信息
    TeacherInfo getTeacherInfo(@Param("username") String username);

    //根据用户名查询个人账号信息
    User getUserInfo(@Param("username") String username);

    /**
     * 更新用户密码
     *
     */
    int upPassword(@Param("username") String username,@Param("password") String password);

}
