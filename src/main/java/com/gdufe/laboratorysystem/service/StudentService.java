package com.gdufe.laboratorysystem.service;

import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.User;

import java.util.List;

public interface StudentService {
    User getStudentUser(String username);

    //获取显示的公告
    List<Notice> getShowNotice();
}
