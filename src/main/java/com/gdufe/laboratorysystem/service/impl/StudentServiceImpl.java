package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.*;
import com.gdufe.laboratorysystem.entity.*;

import com.gdufe.laboratorysystem.service.StudentService;
import com.gdufe.laboratorysystem.utils.ImgHeadUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

//    @Autowired
//    StudentInfoDao studentInfoDao;

    @Value("${system.user.password.secret}")
    private String secret;
    /*更新认证信息*/
    public static void setLoginUser(UserDetails userDetails) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

    @Override
    public User getStudentUser(String username) {
        System.out.println("sssssssssssssssss");
        User user = studentUserDao.getUserInfo(username);
        return user;
    }

    /**
     * 更新学生账号信息
     * @param file
     * @param user
     * @return
     */
    @Override
    @Transactional
    public HashMap upStudentUserInfo(MultipartFile file, User user){
        HashMap hashMap = new HashMap();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            user.setUsername(currentUserName);
            hashMap.put("msg","未登录");
            hashMap.put("status","201");
        }
        String urlPasth=null;
        if (file != null){
            urlPasth = ImgHeadUtils.imgHead(file);
        }

        if (urlPasth != null){
            user.setHeadPortrait(urlPasth);
        }

        int i = studentUserDao.upStudentUser(user);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","更新成功");
            //更新信息

            user = studentUserDao.getStudent(user.getUsername());
            setLoginUser(user);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","更新失败，请重试！！");
        }
        return hashMap;
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

            //判断实验室状态
            String laboratoryStatus = laboratoryInfoDao.getLaboratoryStatus(reserve.getLabid());
            if (laboratoryStatus == null || laboratoryStatus.equals("")  || !laboratoryStatus.equals("开放")){
                System.out.println("laboratoryStatus = " + laboratoryStatus);
                return laboratoryStatus;
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

    /*
    预记录列表
     */
    @Override
    public List<Reserve> getReserveList(Reserve reserve,LaboratoryInfo laboratoryInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            reserve.setUsername(currentUserName);
        }
        reserve.setUserType("student");
        List<Reserve> reserveList = reserveDao.getReserveList(reserve,laboratoryInfo);
        return reserveList;
    }

    /**
     * 预约详情记录
     * @return
     */
    @Override
    public Reserve getDetailReserve(String id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
           String username = authentication.getName();
            Reserve detailReserve = reserveDao.getDetailReserve(id, username);
            return detailReserve;
        }
        return null;
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
        User userInfo = studentUserDao.getUserInfo(username);

        //验证旧密码是否相同
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        if(encoder.matches(oldPassword,userInfo.getPassword())){
            System.out.println("newPassword"+encoder.encode(newPassword));
            int i = studentUserDao.upPassword(username, encoder.encode(newPassword));
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
    public StudentUser getStudentInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String username = authentication.getName();
            StudentUser studentInfo = studentUserDao.getStudentInfo(username);
            System.out.println(studentInfo.toString());
            System.out.println();
            return studentInfo;
        }

        return null;
    }

    //删除预定
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
