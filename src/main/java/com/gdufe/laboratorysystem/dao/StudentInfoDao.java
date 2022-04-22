package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.StudentInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: LaboratorySystem
 * @description: 个人信息
 * @author: chen weize
 * @create: 2022-03-06 19:45
 * @version: 1.0
 */
@Repository
public interface StudentInfoDao {

    //根据用户名查找个人信息
//    Map<String,Object> getStudentInfo(String username);

    //根据条件查询用户信息结果集
    List<StudentInfo> getStudentInfoAll(StudentInfo studentInfo);


    StudentInfo getStudentInfo(String usernam);

    /**
     * 学生个人信息列表
     */
    List<StudentInfo> getStudentInfoList(StudentInfo studentInfo);

    //批量添加学生信息
    int addStudentInfoList(List<StudentInfo> studentInfoList);

    /**
     * 批量删除学生个人信息
     */
    int delStudentInfoList(String[] ids);
    /**
     * 更新学生个人信息
     *
     */
    int upStudentInfo(StudentInfo studentInfo);

    //添加学生个人信息
    int addStudentInfo(StudentInfo studentInfo);

    //是否存在学生个人信息账号
    boolean existUsername(String username);
}
