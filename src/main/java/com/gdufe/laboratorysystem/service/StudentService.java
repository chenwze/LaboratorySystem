package com.gdufe.laboratorysystem.service;

import com.gdufe.laboratorysystem.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StudentService {
    //获取用户信息
    User getStudentUser(String username);


    //更新学生账号信息
    HashMap upStudentUserInfo(MultipartFile file, User name);

    //获取显示的公告
    List<Notice> getShowNotice();

    //查找实验室列表
    List<LaboratoryInfo> getLaboratoryInfoList(LaboratoryInfo laboratoryInfo,String reserve);

    //根据labid查询某个实验室信息
    Map getLaboratoryInfo(String labid);

    //修改用户密码
    boolean upPassword(String oldPassword,String newPassword);

    /**
     * 添加预约记录
     * @param reserve
     * @return
     */
     String addReserve(Reserve reserve);

     /**
      *
      * */
    List<Reserve> getReserveList(Reserve reserve,LaboratoryInfo laboratoryInfo);

    /**
     * 查看详情预约记录
     */
    Reserve getDetailReserve(String id);

    /**
     * 获取个人信息表
     */
    StudentUser getStudentInfo();

    /**
     * 删除预约记录
     * @param id
     * @return
     */
    boolean delReserve(String id);


}
