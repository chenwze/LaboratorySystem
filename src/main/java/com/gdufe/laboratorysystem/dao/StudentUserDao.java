package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.StudentUser;
import com.gdufe.laboratorysystem.entity.User;
import org.apache.ibatis.annotations.Param;
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
    StudentUser getStudentInfo(@Param("username") String username);

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



    //根据条件查询用户信息结果集
    List<StudentUser> getStudentInfoAll(StudentUser studentInfo);


//    StudentInfo getStudentInfo(String usernam);

    /**
     * 学生个人信息列表
     */
    List<StudentUser> getStudentInfoList(StudentUser studentInfo);

    //批量添加学生信息
    int addStudentInfoList(List<StudentUser> studentInfoList);

    /**
     * 批量删除学生个人信息
     */
    int delStudentInfoList(String[] ids);
    /**
     * 更新学生个人信息
     *
     */
    int upStudentInfo(StudentUser studentInfo);

    //添加学生个人信息
    int addStudentInfo(StudentUser studentInfo);

    //是否存在学生个人信息账号
    boolean existUsername(String username);
}
