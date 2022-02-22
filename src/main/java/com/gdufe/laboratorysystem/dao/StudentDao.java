package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


//操作学生表
@Repository
public interface StudentDao {
    /**
     * 根据学生用户名查询
     */
   Student  getStudent(@Param("name") String name);

}
