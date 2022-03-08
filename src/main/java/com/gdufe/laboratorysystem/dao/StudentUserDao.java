package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.StudentInfo;
import com.gdufe.laboratorysystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


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


}
