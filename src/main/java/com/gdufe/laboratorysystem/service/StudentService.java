package com.gdufe.laboratorysystem.service;

import com.gdufe.laboratorysystem.dao.LaboratoryInfoDao;
import com.gdufe.laboratorysystem.entity.LaboratoryInfo;
import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.Reserve;
import com.gdufe.laboratorysystem.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface StudentService {
    User getStudentUser(String username);

    //获取显示的公告
    List<Notice> getShowNotice();

    //查找实验室列表
    List<LaboratoryInfo> getLaboratoryInfoList(LaboratoryInfo laboratoryInfo,String reserve);

    //根据labid查询某个实验室信息
    Map getLaboratoryInfo(String labid);

    /**
     * 添加预约记录
     * @param reserve
     * @return
     */
    public boolean addReserve(Reserve reserve);

    List<Reserve> getReserveList(Reserve reserve,LaboratoryInfo laboratoryInfo);
}
