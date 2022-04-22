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

    //更新公告状态
    HashMap upNoticeStatus(Notice notice);

    //获取密码
    String getPassword();

    //管理员账号信息
    User getAdminUser();

    //更新管理员账号信息
    HashMap upAdminUserIfo(MultipartFile file,User name);
    //学生列表
    List<User> getStudentUserList(User user);

    //学生用户
    User getStudentUser(String username);

    //批量添加学生
    int addStudentUserList(List<User>  userList);

    //批量删除学生
    int delStudentUserList(String[] ids);

    /**
     * 添加单一学生用户
     */
    int addStudentUser(User user);

    //重置学生账号密码
    HashMap resetStudentPasssword(String username);

    //修改学生账号信息
    HashMap upStudentUserInfo(MultipartFile file,User user);

    //老师列表
    List<User> getTeacherUserList(User user);

    //批量添加老师
    int addTeacherUserList(List<User> userList);

    //批量删除老师
    int delTeacherUserList(String[] ids);

    //更新老师账号信息
    HashMap upTeacherUserInfo(MultipartFile file,User user);

    /**
     * 添加单一老师用户
     */
    int addTeacherUser(User user);

    /**
     * 重置老师账号密码
     */
    HashMap resetTeacherPasssword(String username);

    //学生用户
    User getTeacherUser(String username);


    /**
     * 学生个人信息列表
     * @param studentInfo
     * @return
     */
    List<StudentInfo> getStudentInfoList(StudentInfo studentInfo);

    //批量添加学生个人信息
    int addStudentInfoList(List<StudentInfo> studentInfoList);
    /**
     * 批量删除学生个人信息
     */
    int delStudentInfoList(String[] dateList);

    /**
     * 学生个人详情信息
     * @param username
     * @return
     */
    StudentInfo getStudentInfo(String username);

    //添加学生个人信息
    HashMap addStudentInfo(StudentInfo studentInfo);

    //修改保存学生个人信息
    HashMap upStudentInfo(StudentInfo studentInfo);


    /**
     * 老师个人信息列表
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

    //添加老师个人信息
    HashMap addTeacherInfo(TeacherInfo teacherInfo);

    //更新老师个人信息
    HashMap upTeacherInfo(TeacherInfo teacherInfo);

    //批量添加老师个人信息
    int addTeacherInfoList(List<TeacherInfo> teacherInfoList);

    /**
     * 批量删除老师个人信息
     */
    int delTeacherInfoList(String[] ids);

    //获取实验详情信息
    LaboratoryInfo getLaboratoryInfo(String labid);

    //添加预约记录
    HashMap addReserve(String labid ,String reserveTime);

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

    //添加实验室
    HashMap addLaboratoryInfo(LaboratoryInfo laboratoryInfo);


    //表格批量添加实验室
    HashMap addLaboratoryInfoList(List<LaboratoryInfo> laboratoryInfoList);

    //批量删除实验室
    HashMap delLaboratoryInfoList(String[] ids);

    //更新实验室信息
    HashMap upLaboratoryInfo(LaboratoryInfo laboratoryInfo);

    //更新实验室状态
    HashMap upStatus(String labid ,String status);

    //取消预约
    HashMap delReserveAdmin(String id);

    //预约详情信息
    Reserve getDetailReserve(String id);

    //批量删除预约记录
    HashMap delReserveInfoList(String[] ids);

    //更新管理员密码
    HashMap upAdminPassword(String oldPassword,String newPassword);
    
//    HashMap upPassword(String newPassword);

    //超级管理员更新密码
    HashMap upPassword(String username,String password);

    //获取实验是labid
    String getLaboratoryId(LaboratoryInfo laboratoryInfo);

    //实验室物品列表
    List<LaboratoryThing> getLaboratoryThingList(LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo);

    //添加物品
    HashMap addLabThing(LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo);

    //批量删除物品
    HashMap delLaboratoryThingList(String[] ids);

    //批量表格添加物品
    HashMap addLaboratoryThingList(List<LaboratoryThing> laboratoryThingList);

    //获取物品详情信息
    LaboratoryThing getLaboratoryThing(String id);

    //修改保存物品信息
    HashMap upLaboratoryThing(LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo);
}
