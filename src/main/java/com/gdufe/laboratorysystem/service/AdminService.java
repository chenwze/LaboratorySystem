package com.gdufe.laboratorysystem.service;

import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.User;

import java.util.List;

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

    //公告详情信息
    Notice getdetailsNoticePage(Notice notice);

    //获取公告列表
    List<Notice> getNoticeList(Notice notice);

    //批量删除公告
    int delNoticeList(String[] ids);

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


}
