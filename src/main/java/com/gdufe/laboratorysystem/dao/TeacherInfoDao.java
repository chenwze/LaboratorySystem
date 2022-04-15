package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.StudentInfo;
import com.gdufe.laboratorysystem.entity.TeacherInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherInfoDao {
    //根据用户名查找个人信息
//    Map<String,Object> getTeacherInfo(String username);

    /**
     * 老师个人信息列表
     * @param teacherInfo
     * @return
     */
    List<TeacherInfo> getTeacherinfoList(TeacherInfo teacherInfo);

    //获取单个老师信息
    TeacherInfo getTeacherInfo(String usernam);

    /**
     * 更新修改老师个人信息
     */
    int upTeacherInfo(TeacherInfo teacherInfo);
}
