package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.LaboratoryInfo;
import com.gdufe.laboratorysystem.entity.LaboratoryThing;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaboratoryThingDao {
    //    获取实验室物品列表
    List<LaboratoryThing> getLaboratoryThingList(LaboratoryThing laboratory_thing, LaboratoryInfo laboratory_info);

    //添加物品
    int addLabThing(LaboratoryThing laboratoryThing);

    //批量删除物品
    int delLabThingList(String[] ids);

   //批量表格添加物品信息
    int addLaboratoryThingList(List<LaboratoryThing> laboratoryThingList);

    //获取实验室物品信息
    LaboratoryThing getLaboratoryThing(String id);

//    更新物品信息
    int upLaboratoryThing(LaboratoryThing laboratoryThing);
}
