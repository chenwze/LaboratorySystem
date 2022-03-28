package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.TeacherInfo;
import com.gdufe.laboratorysystem.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


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


    List<User> getTeacherUserList(User user);

    //批量添加用户
    int addTeacherUserList(List<User> userList);

    //添加单一学生用户
    int addTeacherUser(User user);

    //批量删除公告
    int delTeacherUserList(String[] ids);
}
