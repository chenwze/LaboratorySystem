package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.StudentInfo;
import com.gdufe.laboratorysystem.entity.VO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    Map<String,Object> getStudentInfo(String username);

    //根据条件查询用户信息结果集
    List<StudentInfo> getStudentInfoAll(StudentInfo studentInfo);

}
