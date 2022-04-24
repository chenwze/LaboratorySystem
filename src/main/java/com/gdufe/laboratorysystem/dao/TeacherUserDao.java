package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.TeacherUser;
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
    TeacherUser getTeacherInfo(@Param("username") String username);

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

    /**
     * 邮箱是否为空
     */

    boolean nonEmptyEmail(String email);

    /**
     * 老师个人信息列表
     * @param teacherInfo
     * @return
     */
    List<TeacherUser> getTeacherinfoList(TeacherUser teacherInfo);

//    //获取单个老师信息
//    TeacherInfo getTeacherInfo(String usernam);

    /**
     * 更新修改老师个人信息
     */
    int upTeacherInfo(TeacherUser teacherInfo);

    /**
     * 老师账号是否存在
     */
    boolean existUsername(String username);

    /*
    添加老师个人信息
     */
    int addTeacherInfo(TeacherUser teacherInfo);

    //批量添加老师个人信息
    int addTeacherInfoList(List<TeacherUser> teacherInfoList);

    //批量删除老师个人信息
    int delTeacherInfoList(String[] ids);
}
