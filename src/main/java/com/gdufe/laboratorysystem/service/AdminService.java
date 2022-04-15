package com.gdufe.laboratorysystem.service;

import com.gdufe.laboratorysystem.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-25 00:02
 * @version: 1.0
 */
public interface AdminService {
    LaboratoryThing add(LaboratoryThing laboratoryThing);
    //发布公告
    public Notice addNotice(Notice notice);

    //修改公告
    int upNotice(Notice notice);

    //公告详情信息
    Notice getdetailsNoticePage(Notice notice);

    //获取公告列表
    List<Notice> getNoticeList(Notice notice);

    //批量删除公告
    int delNoticeList(String[] ids);

    //获取密码
    String getPassword();

    //管理员账号信息
    User getAdminUser();

    //更新管理员账号信息
    HashMap upAdminUserIfo(MultipartFile file,User name);
    //学生列表
    List<User> getStudentUserList(User user);

    //批量添加学生
    int addStudentUserList(List<User> userList);

    //批量删除学生
    int delStudentUserList(String[] ids);

    /**
     * 添加单一学生用户
     */
    int addStudentUser(User user);




    //老师列表
    List<User> getTeacherUserList(User user);

    //批量添加老师
    int addTeacherUserList(List<User> userList);

    //批量删除老师
    int delTeacherUserList(String[] ids);

    /**
     * 添加单一老师用户
     */
    int addTeacherUser(User user);

    /**
     * 学生个人信息列表
     * @param studentInfo
     * @return
     */
    List<StudentInfo> getStudentInfoList(StudentInfo studentInfo);

    /**
     * 学生个人详情信息
     * @param username
     * @return
     */
    StudentInfo getStudentInfo(String username);


    /**
     * 学生个人信息列表
     * @param teacherInfo
     * @return
     */
    List<TeacherInfo> getTeacherInfoList(TeacherInfo teacherInfo);

    /**
     * 老师个人详情信息
     * @param username
     * @return
     */
    TeacherInfo getTeacherInfo(String username);

    /**
     * 获取实验室信息列表
     * @param laboratoryInfo
     * @param reserve
     * @return
     */
    List<LaboratoryInfo> getLaboratoryInfoList(LaboratoryInfo laboratoryInfo, String reserve);

    /**
     * 实验室预约记录
     * @param reserve
     * @param laboratoryInfo
     * @return
     */

    List<Reserve> getAdminReserveList(Reserve reserve,LaboratoryInfo laboratoryInfo);
    //更新管理员密码
    HashMap upAdminPassword(String oldPassword,String newPassword);
    
//    HashMap upPassword(String newPassword);

    //超级管理员更新密码
    HashMap upPassword(String username,String password);
}
