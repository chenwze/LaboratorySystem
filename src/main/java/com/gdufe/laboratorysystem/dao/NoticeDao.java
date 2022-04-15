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

    //公告详情信息
    Notice getdetailsNoticePage(Notice notice);
    //添加公告
    int addNotice(Notice notice);

    //修改公告
    int upNotice(Notice notice);

    //管理员查看公告列表
    List<Notice> getNoticeList(Notice notice);

    //批量删除公告
    int delNoticeList(String[] ids);
}
