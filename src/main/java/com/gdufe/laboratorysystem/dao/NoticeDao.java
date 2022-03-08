package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-07 15:53
 * @version: 1.0
 */
@Repository
public interface NoticeDao {
    //获取公告
    List<Notice> getShowNotice();
}
