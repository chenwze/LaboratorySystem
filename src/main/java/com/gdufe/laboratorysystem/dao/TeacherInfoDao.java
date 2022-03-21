package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.StudentInfo;
import com.gdufe.laboratorysystem.entity.TeacherInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherInfoDao {
    //根据用户名查找个人信息
//    Map<String,Object> getTeacherInfo(String username);

    //根据条件查询用户信息结果集
    List<TeacherInfo> getStudentInfoAll(TeacherInfo teacherInfo);


    TeacherInfo getTeacherInfo(String usernam);

}
