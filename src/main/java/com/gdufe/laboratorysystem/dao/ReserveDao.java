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
     * @param labid
     * @return
     */
    List<Reserve> getReserveTimeList(@Param("labid") String labid);

    /**
     * 添加预约记录
     */
    int addReserve(Reserve reserve);

    /**
     *    查询用户个人的预约记录
      */
    List<Reserve> getReserveList(Reserve reserve, LaboratoryInfo laboratoryInfo);
}
