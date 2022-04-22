package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.LaboratoryInfo;
import com.gdufe.laboratorysystem.entity.Reserve;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 预约表reserve
 * @author CWZ
 */
@Repository
public interface ReserveDao {
    /**
     * 获取已预约时间列表,根据实验室唯一编号labid，预约时间
     * @param username
     * @return
     */
    List<Reserve> getReserveTimeList(@Param("username") String username);

    /**
     * 添加预约记录
     */
    int addReserve(Reserve reserve);

    /**
     *    查询用户个人的预约记录
      */
    List<Reserve> getReserveList(Reserve reserve, LaboratoryInfo laboratoryInfo);


    /**
     * 取消预约
     */
    int delReserve(@Param("id") String id ,@Param("username") String username);

    //getDetailReserve预约详情记录
    Reserve getDetailReserve(@Param("id") String id,@Param("username") String username);

    //管理员预约详情记录
    Reserve getDetailReserveAdmin(@Param("id") String id);
//    管理员查询实验室预约记录
    List<Reserve> getAdminReserveList(Reserve reserve, LaboratoryInfo laboratoryInfo);

    //管理员取消预约
    int delReserveAdmin(String id);

    //批量删除记录
    int delReserveInfoList(String[] ids);
}
