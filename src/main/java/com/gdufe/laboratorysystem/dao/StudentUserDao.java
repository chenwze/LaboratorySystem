package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.StudentInfo;
import com.gdufe.laboratorysystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


//操作学生表
@Repository
public interface StudentUserDao {
    /**
     * 根据学生用户名查询
     */
   User getStudent(@Param("username") String username);

   //根据用户名查询个人信息
    StudentInfo getStudentInfo(@Param("username") String username);

    //根据用户名查询个人账号信息
    User getUserInfo(@Param("username") String username);

    /**
     * 更新用户密码
     *
     */
    int upPassword(@Param("username") String username,@Param("password") String password);

    List<User> getStudentUserList(User user);


    //批量添加用户
    int addStudentUserList(List<User> userList);

    //添加单一学生用户
    int addStudentUser(User user);

    //批量删除公告
    int delStudentUserList(String[] ids);
}
