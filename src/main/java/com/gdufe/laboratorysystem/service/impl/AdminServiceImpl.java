package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.*;
import com.gdufe.laboratorysystem.entity.*;
import com.gdufe.laboratorysystem.service.AdminService;
import com.gdufe.laboratorysystem.utils.ImgHeadUtils;
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
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-25 00:02
 * @version: 1.0
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    NoticeDao noticeDao;
    
    @Autowired
    StudentUserDao studentUserDao;
    
    @Autowired
    TeacherUserDao teacherUserDao;

    @Autowired
    StudentInfoDao studentInfoDao;
    @Autowired
    TeacherInfoDao teacherInfoDao;
    @Autowired
    LaboratoryInfoDao laboratoryInfoDao;
    @Autowired
    ReserveDao reserveDao;
    @Autowired
    AdminUserDao adminUserDao;
    @Autowired
    LaboratoryThingDao laboratoryThingDao;
    
    @Value("${system.user.password.secret}")
    private String secret;
    /*更新认证信息*/
    public static void setLoginUser(UserDetails userDetails) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }


    //发布公告
    @Override
    public Notice addNotice(Notice notice) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            notice.setAdminUsername(username);
            notice.setId(UUID.randomUUID().toString().replaceAll("-",""));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            notice.setTime(df.format(new Date()));
            System.out.println(notice.toString());
            int i = noticeDao.addNotice(notice);
            if (i==1){
                System.out.println(notice.toString());
                return notice;
            }
        }else{
            return null;
        }

        return null;
    }

    @Override
    public LaboratoryThing add(LaboratoryThing laboratoryThing) {
        LaboratoryThing add = laboratoryThingDao.add(laboratoryThing);
        return add;
    }

    /**
     * 修改公告
     */
    @Override
    public int upNotice(Notice notice) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            notice.setAdminUsername(username);
//            notice.setId(UUID.randomUUID().toString().replaceAll("-",""));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            notice.setTime(df.format(new Date()));
            System.out.println(notice.toString());
            int i = noticeDao.upNotice(notice);
            if (i==1){
                System.out.println(notice.toString());
                return 0;
            }
        }else{
            return 0;
        }
        return 0;
    }
    /**
     * 公告详情信息
     * @param notice
     * @return
     */
    @Override
    public Notice getdetailsNoticePage(Notice notice) {
        notice=noticeDao.getdetailsNoticePage(notice);
        return notice;
    }

    /**
     * 获取公告列表
     * @param notice
     * @return
     */
    @Override
    public List<Notice> getNoticeList(Notice notice) {

        List<Notice> noticeList = noticeDao.getNoticeList(notice);

        return noticeList;
    }

    /**
     *批量删除公告
     * @param ids
     * @return
     */
    @Override
    public int delNoticeList(String[] ids) {
        int i = noticeDao.delNoticeList(ids);
        return i;
    }


    /**
     * 查看管理账号信息
     */
    @Override
    public User getAdminUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            User admin = adminUserDao.getAdmin(currentUserName);
            return admin;
        }
        return null;
    }

    /**
     * 更新管理账号信息
     * @return
     */
    @Override
    @Transactional
    public HashMap upAdminUserIfo(MultipartFile file, User user){
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

        int i = adminUserDao.upAdminUserInfo(user);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","更新成功");
            //更新信息

            user = adminUserDao.getAdmin(user.getUsername());
            setLoginUser(user);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","更新失败，请重试！！");
        }
        return hashMap;
    }

    //更新管理员账号密码
    @Override
    public HashMap upAdminPassword(String oldPassword, String newPassword){
        HashMap hashMap = new HashMap();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            User admin = adminUserDao.getAdmin(currentUserName);
            boolean matches = encoder.matches(oldPassword, admin.getPassword());
            boolean matches1 = encoder.matches(newPassword, admin.getPassword());
            if (matches1){
                hashMap.put("msg","旧密码不能和新密码一样！");
                hashMap.put("status","");
            }

            if (matches){
                int i = adminUserDao.upPassword(currentUserName, encoder.encode(newPassword));

                if (i==1){
                    hashMap.put("msg","修改成功");
                    hashMap.put("status","200");
                    User user = adminUserDao.getAdmin(currentUserName);
                    setLoginUser(user);
                }
            }else {
                hashMap.put("msg","旧密码错误");
                hashMap.put("status","");
            }

            return  hashMap;
        }
        hashMap.put("msg","未登录");
        hashMap.put("status","-1");
        return  hashMap;
    }
    /**
     * 学生用户列表
     * @param user
     * @return
     */
    @Override
    public List<User> getStudentUserList(User user) {
        System.out.println("user.toString() = " + user.toString());
        List<User> studentUserList = studentUserDao.getStudentUserList(user);
        return studentUserList;
    }


    //批量添加学生用户
    @Override
    @Transactional
    public int addStudentUserList(List<User> userList) {
        int i = studentUserDao.addStudentUserList(userList);
        return i;
    }

    /**
     * 添加学生用户
     * @param user
     * @return
     */
    @Override
    public int addStudentUser(User user) {

        System.out.println("+++++++++++++++++++"+user.toString());
        int i = studentUserDao.addStudentUser(user);
        return i;
    }

    /**
     * 批量删除学生账号
     * @param ids
     * @return
     */
    @Override
    public int delStudentUserList(String[] ids) {
        int i = studentUserDao.delStudentUserList(ids);
        return i;
    }


    /**
     * 老师用户列表
     * @param user
     * @return
     */
    @Override
    public List<User> getTeacherUserList(User user) {
        System.out.println("user.toString() = " + user.toString());
        List<User> teacherUserList = teacherUserDao.getTeacherUserList(user);
        return teacherUserList;
    }


    //批量添加老师用户
    @Override
    @Transactional
    public int addTeacherUserList(List<User> userList) {
        int i = teacherUserDao.addTeacherUserList(userList);
        return i;
    }


    /**
     * 添加老师用户
     * @param user
     * @return
     */
    @Override
    public int addTeacherUser(User user) {

        System.out.println("+++++++++++++++++++"+user.toString());
        int i = teacherUserDao.addTeacherUser(user);
        return i;
    }

    /**
     * 批量删除老师账号
     * @param ids
     * @return
     */
    @Override
    public int delTeacherUserList(String[] ids) {
        int i = teacherUserDao.delTeacherUserList(ids);
        return i;
    }


    /**
     * 学生个人信息列表
     */
    @Override
    public List<StudentInfo> getStudentInfoList(StudentInfo studentInfo) {
        List<StudentInfo> studentInfoList = studentInfoDao.getStudentInfoList(studentInfo);
        return studentInfoList;
    }

    /**
     * 学生个人详情信息
     * @param username
     * @return
     */
    @Override
    public StudentInfo getStudentInfo(String username) {
        StudentInfo studentInfo = studentInfoDao.getStudentInfo(username);
        return studentInfo;
    }

    /**
     * 老师个人信息列表
     */
    @Override
    public List<TeacherInfo> getTeacherInfoList(TeacherInfo teacherInfo) {
        List<TeacherInfo> teacherinfoList = teacherInfoDao.getTeacherinfoList(teacherInfo);
        return teacherinfoList;
    }

    /**
     * 老师个人详情信息
     * @param username
     * @return
     */
    @Override
    public TeacherInfo getTeacherInfo(String username){
        TeacherInfo teacherInfo = teacherInfoDao.getTeacherInfo(username);
        return teacherInfo;
    }

    /**
     * 获取实验室信息列表
     * @param laboratoryInfo
     * @param reserve
     * @return
     */
    @Override
    public List<LaboratoryInfo> getLaboratoryInfoList(LaboratoryInfo laboratoryInfo, String reserve) {
        List<LaboratoryInfo> laboratoryInfoList = laboratoryInfoDao.getAdminLaboratoryInfoList(laboratoryInfo);
        return laboratoryInfoList;
    }

    /**
     * 实验室预约记录
     * @param reserve
     * @param laboratoryInfo
     * @return
     */
    @Override
    public List<Reserve> getAdminReserveList(Reserve reserve, LaboratoryInfo laboratoryInfo) {
        List<Reserve> adminReserveList = reserveDao.getAdminReserveList(reserve, laboratoryInfo);
        return adminReserveList;
    }

    //获取密码
    @Override
    public String getPassword() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            String password = adminUserDao.getPassword(currentUserName);
            return password;
        }
        return null;
    }

//    /**
//     * upPassword
//     * 更新修改管理员密码
//     */
//    @Override
//    public HashMap upPassword(String password) {
//        HashMap map = new HashMap();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            //获取当前用户名
//            String currentUserName = authentication.getName();
//            int i = adminUserDao.upPassword(currentUserName, password);
//            if (i==1){
//                map.put("status",200);
//                map.put("msg","更新成功！");
//                return map;
//            }
//        }
//        map.put("status",201);
//        map.put("msg","更新失败！");
//        return map;
//    }

    /**
     * upPassword
     * 超管更新修改管理员密码
     */
    @Override
    public HashMap upPassword(String username, String password) {
        HashMap map = new HashMap();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            if (currentUserName.equals("10001")){
                int i = adminUserDao.upPassword(username, password);
                if (i==1){
                    map.put("status",200);
                    map.put("msg","更新成功！");
                    return map;
                }

            }else{
                map.put("status",403);
                map.put("msg","权限不足！！");
                return map;
            }

        }
        map.put("status",201);
        map.put("msg","更新失败！");
        return map;
    }


}
