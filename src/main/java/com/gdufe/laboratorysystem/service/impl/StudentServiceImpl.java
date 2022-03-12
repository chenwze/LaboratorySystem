package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.LaboratoryInfoDao;
import com.gdufe.laboratorysystem.dao.NoticeDao;
import com.gdufe.laboratorysystem.dao.ReserveDao;
import com.gdufe.laboratorysystem.dao.StudentUserDao;
import com.gdufe.laboratorysystem.entity.LaboratoryInfo;
import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.Reserve;
import com.gdufe.laboratorysystem.entity.User;

import com.gdufe.laboratorysystem.service.StudentService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-07 11:53
 * @version: 1.0
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentUserDao studentUserDao;

    @Autowired
    NoticeDao noticeDao;

    @Autowired
    ReserveDao reserveDao;

    @Autowired
    LaboratoryInfoDao laboratoryInfoDao;


    @Override
    public User getStudentUser(String username) {
        System.out.println("sssssssssssssssss");
        User user = studentUserDao.getUserInfo(username);
        return user;
    }

    @Override
    public List<Notice> getShowNotice() {
        List<Notice> noticeList = noticeDao.getShowNotice();
        return noticeList;
    }

    //查找实验室列表
    @Override
    public List<LaboratoryInfo> getLaboratoryInfoList(LaboratoryInfo laboratoryInfo,String reserve) {
        List<LaboratoryInfo> laboratoryInfoList = laboratoryInfoDao.getLaboratoryInfoList(laboratoryInfo,reserve);
        return laboratoryInfoList;
    }

    /**
     * 根据labid查询某个实验室信息
     * @param labid 实验室的唯一编号
     * @return LaboratoryInfo 实验室信息
     */
    @Override
    public Map getLaboratoryInfo(String labid) {
        Map map= new HashMap();

        LaboratoryInfo laboratoryInfo = laboratoryInfoDao.getLaboratoryInfo(labid);

        map.put("laboratoryInfo",laboratoryInfo);
        Date date=new Date(System.currentTimeMillis());
        List<Reserve> reserveTimeList = reserveDao.getReserveTimeList(labid);

        JSONObject object = new JSONObject();
        for (Reserve reserve: reserveTimeList) {
            System.out.println(reserve.toString());
            object.put(reserve.getReserveTime(),"");
        }
        System.out.println(object+"++++++++++++++++");
        map.put("reserveTimeList",object);
        return map;
    }

    /**
     * 添加预约记录
     * @param reserve
     * @return
     */
    @Override
    @Transactional
    public boolean addReserve(Reserve reserve){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            reserve.setUsername(currentUserName);

            Object[] toArray = authentication.getAuthorities().toArray();
            for (Object array:toArray) {

                //含有student权限跳转学生页面
                if (array.toString().equals("student")){
                    System.out.println("++++++++++++++++"+array);
                  reserve.setUserType(array.toString());
                }

                //含有teachert权限跳转老师页面
                if (array.toString().equals("teacher")){
                    System.out.println("++++++++++++++++"+array);
                    reserve.setUserType(array.toString());
                }

                //含有admin权限跳转管理员页面
                if(array.toString().equals("admin")){
                    reserve.setUserType(array.toString());
                }
            }
            reserve.setId(UUID.randomUUID().toString().replaceAll("-",""));
            System.out.println(reserve.toString()+"===============");
            int i = reserveDao.addReserve(reserve);
//            System.out.println(reserve.getLaboratoryInfo().toString());
            System.out.println(reserve.toString());
            if (i==1){return true;}
        }
            return false;
    }

    @Override
    public List<Reserve> getReserveList(Reserve reserve,LaboratoryInfo laboratoryInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            reserve.setUsername(currentUserName);
        }
        List<Reserve> reserveList = reserveDao.getReserveList(reserve,laboratoryInfo);
        return reserveList;
    }
}
