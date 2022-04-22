package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.LaboratoryInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: LaboratorySystem
 * @description: 实验室信息
 * @author: chen weize
 * @create: 2022-03-08 10:30
 * @version: 1.0
 */
@Repository
public interface LaboratoryInfoDao {

    //查找实验室列表
    List<LaboratoryInfo> getLaboratoryInfoList( LaboratoryInfo laboratoryInfo, @Param("reserve") String reserve );

    //根据实验室编号labid查找实验室
    LaboratoryInfo getLaboratoryInfo(@Param("labid") String labid);

    //管理员查询实验室列表
    List<LaboratoryInfo> getAdminLaboratoryInfoList( LaboratoryInfo laboratoryInfo );

    //查看实验的状态，开放或其他
    String getLaboratoryStatus(String labid);

    //添加实验室
    int addLaboratoryInfo(LaboratoryInfo laboratoryInfo);

    //表格批量添加实验室
    int addLaboratoryInfoList(List<LaboratoryInfo> laboratoryInfoList);

    //批量删除实验室
    int delLaboratoryInfoList(String[] ids);

    //更新实验室信息
    int upLaboratoryInfo(LaboratoryInfo laboratoryInfo);

    //修改实验室状态
    int upStatus(@Param("labid") String labid ,@Param("status") String status);

    //获取实验室labid
    String getLabid(LaboratoryInfo laboratoryInfo);
}
