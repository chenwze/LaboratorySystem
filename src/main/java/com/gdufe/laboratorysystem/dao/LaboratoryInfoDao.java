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
}
