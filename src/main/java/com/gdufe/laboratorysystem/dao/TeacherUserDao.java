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

    /**
     * 忘记密码，修改密码
     * @param
     * @return
     */
    int upPasswrodEmail(@Param("username") String username,@Param("password") String password
            ,@Param("email") String email);


    List<User> getTeacherUserList(User user);

    //批量添加用户
    int addTeacherUserList(List<User> userList);

    //添加单一学生用户
    int addTeacherUser(User user);

    //批量删除公告
    int delTeacherUserList(String[] ids);

    //更新老师账号个人信息
    int upTeacherUser(User user);

    //设置状态
    int upStatus(String status);

    //获取老师账号邮件
    String getEmail(String username);

    //管理员设置老师老师用户
    int adminSetStatus(String status);
    /**
     * 管理员更新老师账号信息
     */
    int adminUpTeacherUser(User user);
}
