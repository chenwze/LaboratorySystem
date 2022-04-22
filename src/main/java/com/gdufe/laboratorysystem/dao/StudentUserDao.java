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
  * 忘记密码，更新密码
  */
   int upPasswrodEmail(@Param("username") String username,@Param("password") String password
                       ,@Param("email") String email);
 /**
     * 更新用户密码
     *
     */
    int upPassword(@Param("username") String username,@Param("password") String password);

    List<User> getStudentUserList(User user);


    //批量添加用户
    int addStudentUserList(List<User>  userList);

    //添加单一学生用户
    int addStudentUser(User user);

    /**
     * 添加学生用户时邮箱是否存在
     */
    int isEmptyUserEmail(String email);

    //批量删除学生账号
    int delStudentUserList(String[] ids);

    //更新账号状态
    int UpStatus(User user);

    //更新账号信息
    int upStudentUser(User user);

    String getEmail(String username);


    /**
     * 管理员更新学生密码
     */
    int adminUpPassword(@Param("username") String username,@Param("password") String password);

    /**
     * 管理员更新学生状态
     */
    int adminSetStatus(@Param("username") String username,@Param("status") String status);

    //    <!--管理员更新学生账号信息-->
    int adminUpStudentUser(User user);
}
