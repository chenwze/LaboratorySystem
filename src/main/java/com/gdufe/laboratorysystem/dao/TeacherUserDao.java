package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TeacherUserDao {
    /**
     * 根据老师用户名查询
     */
    User getTeacher(@Param("username") String username);
}
