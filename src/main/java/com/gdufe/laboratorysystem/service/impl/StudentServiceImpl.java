package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.NoticeDao;
import com.gdufe.laboratorysystem.dao.StudentUserDao;
import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.User;

import com.gdufe.laboratorysystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-07 11:53
 * @version: 1.0
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentUserDao studentUserDao;

    @Autowired
    NoticeDao noticeDao;

    @Override
    public User getStudentUser(String username) {
        System.out.println("sssssssssssssssss");
        User user = studentUserDao.getUserInfo(username);
        return user;
    }

    @Override
    public List<Notice> getShowNotice() {
        List<Notice> noticeList = noticeDao.getShowNotice();
        return noticeList;
    }
}
