package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.NoticeDao;
import com.gdufe.laboratorysystem.dao.StudentUserDao;
import com.gdufe.laboratorysystem.dao.TeacherUserDao;
import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.User;
import com.gdufe.laboratorysystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-25 00:02
 * @version: 1.0
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    NoticeDao noticeDao;
    
    @Autowired
    StudentUserDao studentUserDao;
    
    @Autowired
    TeacherUserDao teacherUserDao;
    
    //发布公告
    @Override
    public Notice addNotice(Notice notice) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            notice.setAdminUsername(username);
            notice.setId(UUID.randomUUID().toString().replaceAll("-",""));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            notice.setTime(df.format(new Date()));
            System.out.println(notice.toString());
            int i = noticeDao.addNotice(notice);
            if (i==1){
                System.out.println(notice.toString());
                return notice;
            }
        }else{
            return null;
        }
        return null;
    }

    /**
     * 公告详情信息
     * @param notice
     * @return
     */
    @Override
    public Notice getdetailsNoticePage(Notice notice) {
        notice=noticeDao.getdetailsNoticePage(notice);
        return notice;
    }

    /**
     * 获取公告列表
     * @param notice
     * @return
     */
    @Override
    public List<Notice> getNoticeList(Notice notice) {

        List<Notice> noticeList = noticeDao.getNoticeList(notice);

        return noticeList;
    }

    /**
     *批量删除公告
     * @param ids
     * @return
     */
    @Override
    public int delNoticeList(String[] ids) {
        int i = noticeDao.delNoticeList(ids);
        return i;
    }


    /**
     * 学生用户列表
     * @param user
     * @return
     */
    @Override
    public List<User> getStudentUserList(User user) {
        System.out.println("user.toString() = " + user.toString());
        List<User> studentUserList = studentUserDao.getStudentUserList(user);
        return studentUserList;
    }


    //批量添加学生用户
    @Override
    @Transactional
    public int addStudentUserList(List<User> userList) {
        int i = studentUserDao.addStudentUserList(userList);
        return i;
    }

    /**
     * 添加学生用户
     * @param user
     * @return
     */
    @Override
    public int addStudentUser(User user) {

        System.out.println("+++++++++++++++++++"+user.toString());
        int i = studentUserDao.addStudentUser(user);
        return i;
    }

    /**
     * 批量删除学生账号
     * @param ids
     * @return
     */
    @Override
    public int delStudentUserList(String[] ids) {
        int i = studentUserDao.delStudentUserList(ids);
        return i;
    }


    /**
     * 老师用户列表
     * @param user
     * @return
     */
    @Override
    public List<User> getTeacherUserList(User user) {
        System.out.println("user.toString() = " + user.toString());
        List<User> teacherUserList = teacherUserDao.getTeacherUserList(user);
        return teacherUserList;
    }


    //批量添加老师用户
    @Override
    @Transactional
    public int addTeacherUserList(List<User> userList) {
        int i = teacherUserDao.addTeacherUserList(userList);
        return i;
    }


    /**
     * 添加老师用户
     * @param user
     * @return
     */
    @Override
    public int addTeacherUser(User user) {

        System.out.println("+++++++++++++++++++"+user.toString());
        int i = teacherUserDao.addTeacherUser(user);
        return i;
    }

    /**
     * 批量删除老师账号
     * @param ids
     * @return
     */
    @Override
    public int delTeacherUserList(String[] ids) {
        int i = teacherUserDao.delTeacherUserList(ids);
        return i;
    }

}
