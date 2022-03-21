package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.*;
import com.gdufe.laboratorysystem.entity.*;
import com.gdufe.laboratorysystem.service.StudentService;
import com.gdufe.laboratorysystem.service.TeacherService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
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
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    TeacherUserDao teacherUserDao;

    @Autowired
    NoticeDao noticeDao;

    @Autowired
    ReserveDao reserveDao;

    @Autowired
    LaboratoryInfoDao laboratoryInfoDao;

    @Autowired
    TeacherInfoDao teacherInfoDao;

    @Value("${system.user.password.secret}")
    private String secret;

    @Override
    public User getTeacherUser(String username) {
        System.out.println("sssssssssssssssss");
        User user = teacherUserDao.getUserInfo(username);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            List<Reserve> reserveTimeList = reserveDao.getReserveTimeList(currentUserName);
            JSONObject object = new JSONObject();
            for (Reserve reserve : reserveTimeList) {
                System.out.println(reserve.toString());
                object.put(reserve.getReserveTime(), "");
            }
            System.out.println(object + "++++++++++++++++");
            map.put("reserveTimeList", object);
        }
        return map;
    }

    /**
     * 添加预约记录
     * @param reserve
     * @return
     */
    @Override
    @Transactional
    public String addReserve(Reserve reserve){
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
            if (i==1){return reserve.getId();}
        }
            return "false";
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

    //修改用户密码

    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean upPassword( String oldPassword, String newPassword){
        String username=null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            username = authentication.getName();
        }
        System.out.println("username"+username);
        if (username.equals(null)){
            return false;
        }
        //查找用户信息
        User userInfo = teacherUserDao.getUserInfo(username);

        //验证旧密码是否相同
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        if(encoder.matches(oldPassword,userInfo.getPassword())){
            System.out.println("newPassword"+encoder.encode(newPassword));
            int i = teacherUserDao.upPassword(username, encoder.encode(newPassword));
            if (i==1){
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }

    }

    /**
     * 查看个人信息
     * @return
     */
    @Override
    public TeacherInfo getTeacherInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String username = authentication.getName();
            TeacherInfo teacherInfo = teacherInfoDao.getTeacherInfo(username);
            System.out.println(teacherInfo.toString());
            System.out.println();
            return teacherInfo;
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean delReserve(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
           String username = authentication.getName();
            int i = reserveDao.delReserve(id, username);
            if (i==1){
                return true;
            }
        }
        return false;
    }
}