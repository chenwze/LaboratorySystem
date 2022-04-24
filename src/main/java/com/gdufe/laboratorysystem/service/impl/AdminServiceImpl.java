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
     * 更新公告状态
     * @param notice
     * @return
     */
    @Override
    public HashMap upNoticeStatus(Notice notice){
        HashMap hashMap = new HashMap();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            notice.setAdminUsername(currentUserName);
            int i = noticeDao.upNoticeStatus(notice);

            if (i == 1){
                hashMap.put("status","200");
                //1:显示，0 隐藏
                if (notice.getDisplay() !=null && notice.getDisplay().equals("1")){
                    hashMap.put("msg","公告已显示!");
                }else {
                    hashMap.put("msg","公告已隐藏！");
                }
                return hashMap;

            }else {
                hashMap.put("status","201");
                hashMap.put("msg","设置失败，请稍后重试！！");
                return hashMap;
            }

        }else{  //未登录
            hashMap.put("status","201");
            hashMap.put("msg","未登录账号！！");
            return hashMap;
        }
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
//            hashMap.put("msg","未登录");
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

    /**
     * 学生用户
     * @param username
     * @return
     */
    @Override
    public User getStudentUser(String username){
        User studentUser = studentUserDao.getUserInfo(username);
        return studentUser;
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
        int emptyUserEmail = studentUserDao.isEmptyUserEmail(user.getEmail());
        if(emptyUserEmail>0){
            return 10;
        }
        System.out.println("+++++++++++++++++++"+user.toString());
        int i = studentUserDao.addStudentUser(user);
        return i;
    }

    /**
     * 重置学生账号密码
     * @param username
     * @return
     */
    @Override
    public  HashMap resetStudentPasssword(String username){
        HashMap hashMap = new HashMap();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        String password = encoder.encode("111111");
//        int i = studentUserDao.upPassword(username, password);
        int i = studentUserDao.upPassword(username, encoder.encode("111111"));
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","重置密码成功，你的密码是：111111");
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","重置密码失，请稍后重试！！");
            return hashMap;
        }
    }

    //修改学生账号信息
    @Override
    public HashMap upStudentUserInfo(MultipartFile file,User user){
        HashMap hashMap = new HashMap();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            //获取当前用户名
////            String currentUserName = authentication.getName();
////            user.setUsername(currentUserName);
////            hashMap.put("msg","未登录");
//            hashMap.put("status","201");
//        }
        String urlPasth=null;
        if (file != null && !file.isEmpty() ){
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

//            user = adminUserDao.getAdmin(user.getUsername());
//            setLoginUser(user);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","更新失败，请重试！！");
        }
        return hashMap;
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
        boolean nonEmptyEmail = teacherUserDao.nonEmptyEmail(user.getEmail());
        System.out.println(nonEmptyEmail);
        if (!nonEmptyEmail){
            int i = teacherUserDao.addTeacherUser(user);
            return i;
        }else{
            return 10;
        }
//        int i = teacherUserDao.addTeacherUser(user);
//        return i;
    }

    /**
     * 重置老师账号密码
     * @param username
     * @return
     */
    @Override
    public HashMap resetTeacherPasssword(String username){
        HashMap hashMap = new HashMap();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        String password = encoder.encode("111111");
//        int i = studentUserDao.upPassword(username, password);
        int i = teacherUserDao.upPassword(username, encoder.encode("111111"));
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","重置密码成功，你的密码是：111111");
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","重置密码失，请稍后重试！！");
            return hashMap;
        }

    }

    /**
     * 老师用户
     */
    @Override
    public User getTeacherUser(String username){
        User teacher = teacherUserDao.getTeacher(username);
        return teacher;
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

    //修改老师账号信息
    @Override
    public HashMap upTeacherUserInfo(MultipartFile file,User user){
        HashMap hashMap = new HashMap();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            //获取当前用户名
////            String currentUserName = authentication.getName();
////            user.setUsername(currentUserName);
////            hashMap.put("msg","未登录");
//            hashMap.put("status","201");
//        }
        String urlPasth=null;
        if (file != null && !file.isEmpty() ){
            urlPasth = ImgHeadUtils.imgHead(file);
        }

        if (urlPasth != null){
            user.setHeadPortrait(urlPasth);
        }

        int i = teacherUserDao.upTeacherUser(user);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","更新成功");
            //更新信息

//            user = adminUserDao.getAdmin(user.getUsername());
//            setLoginUser(user);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","更新失败，请重试！！");
        }
        return hashMap;
    }

    /**
     * 学生个人信息列表
     */
    @Override
    public List<StudentUser> getStudentInfoList(StudentUser studentInfo) {
        List<StudentUser> studentInfoList = studentUserDao.getStudentInfoList(studentInfo);
        return studentInfoList;
    }

    //批量添加学生个人信息
    @Override
    public int addStudentInfoList(List<StudentUser> studentInfoList){
        int i = studentUserDao.addStudentInfoList(studentInfoList);
        return i;
    }

    /**
     * 批量删除学生个人信息
     */
    @Override
    public int delStudentInfoList(String[] ids){
        int i = studentUserDao.delStudentInfoList(ids);
        return i;
    }

    /**
     * 学生个人详情信息
     * @param username
     * @return
     */
    @Override
    public StudentUser getStudentInfo(String username) {
        StudentUser studentInfo = studentUserDao.getStudentInfo(username);
        return studentInfo;
    }

    //添加学生个人信息
    @Override
    public HashMap addStudentInfo(StudentUser studentInfo){
        HashMap hashMap = new HashMap();

//        System.out.println("existsuername"+studentUserDao.existUsername(studentInfo.getUsername()));
        if (studentUserDao.existUsername(studentInfo.getUsername())){
            hashMap.put("status","201");
            hashMap.put("msg","账号/学号已存在！！");
            return hashMap;
        }
        int i = studentUserDao.addStudentInfo(studentInfo);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功！！");
        }else {
            hashMap.put("status","201");
            hashMap.put("msg","添加失败！！");
        }
        return hashMap;
    }

    //修改保存学生个人信息
    @Override
    public HashMap upStudentInfo(StudentUser studentInfo){
        HashMap hashMap = new HashMap();

        int i = studentUserDao.upStudentInfo(studentInfo);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","修改成功！！");
        }else {
            hashMap.put("status","201");
            hashMap.put("msg","修改失败！！");
        }
        return hashMap;
    }



    /**
     * 老师个人信息列表
     */
    @Override
    public List<TeacherUser> getTeacherInfoList(TeacherUser teacherInfo) {
        List<TeacherUser> teacherinfoList = teacherUserDao.getTeacherinfoList(teacherInfo);
        return teacherinfoList;
    }


    /**
     * 老师个人详情信息
     * @param username
     * @return
     */
    @Override
    public TeacherUser getTeacherInfo(String username){
        TeacherUser teacherInfo = teacherUserDao.getTeacherInfo(username);
        return teacherInfo;
    }

    //添加老师个人信息
    @Override
    public HashMap addTeacherInfo(TeacherUser teacherInfo){
        HashMap hashMap = new HashMap();
        if (teacherUserDao.existUsername(teacherInfo.getUsername())){
            hashMap.put("status","201");
            hashMap.put("msg","账号/学号已存在！！");
            return hashMap;
        }
        int i = teacherUserDao.addTeacherInfo(teacherInfo);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功！！");
        }else {
            hashMap.put("status","201");
            hashMap.put("msg","添加失败！！");
        }
        return hashMap;
    }

    //更新老师个人信息
    @Override
    public HashMap upTeacherInfo(TeacherUser teacherInfo){
        HashMap hashMap = new HashMap();
        int i = teacherUserDao.upTeacherInfo(teacherInfo);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","修改成功！！");
        }else {
            hashMap.put("status","201");
            hashMap.put("msg","修改失败！！");
        }
        return hashMap;
    }

    //批量添加老师个人信息
    @Override
    public int addTeacherInfoList(List<TeacherUser> teacherInfoList){
        int i = teacherUserDao.addTeacherInfoList(teacherInfoList);
        return i;
    }

    /**
     * 批量删除老师个人信息
     */
    @Override
    public int delTeacherInfoList(String[] ids){
        int i = teacherUserDao.delTeacherInfoList(ids);
        return i;
    }


    //获取实验详情信息
    @Override
    public LaboratoryInfo getLaboratoryInfo(String labid){
        LaboratoryInfo laboratoryInfo = laboratoryInfoDao.getLaboratoryInfo(labid);
        return laboratoryInfo;
    }

    //添加预约记录
    @Override
    public HashMap addReserve(String labid ,String reserveTime){
        HashMap hashMap = new HashMap();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
//            String currentUserName = authentication.getName();
//            user.setUsername(currentUserName);
//            hashMap.put("msg","未登录");
            Reserve reserve = new Reserve();
            reserve.setId(UUID.randomUUID().toString().replaceAll("-",""));
            reserve.setLabid(labid);
            reserve.setReserveTime(reserveTime);
            reserve.setUsername(authentication.getName());
            reserve.setUserType("admin");
            int i = reserveDao.addReserve(reserve);

            if (i==1){
                hashMap.put("status","200");
                hashMap.put("msg","预约成功！！");
                return hashMap;
            }else{
                hashMap.put("status","201");
                hashMap.put("msg","预约失败！！");
                return hashMap;
            }


        }else{
            hashMap.put("status","201");
            hashMap.put("msg","账号未登录！！");
            return hashMap;
        }

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

    //添加实验室
    @Override
    public HashMap addLaboratoryInfo(LaboratoryInfo laboratoryInfo){
        HashMap hashMap =new HashMap();
        laboratoryInfo.setLabid(UUID.randomUUID().toString().replaceAll("-",""));
        int i = laboratoryInfoDao.addLaboratoryInfo(laboratoryInfo);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功！！");
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","添加失败！！");
        }
        return hashMap;
    }

    //表格批量添加实验室
    @Override
    public HashMap addLaboratoryInfoList(List<LaboratoryInfo> laboratoryInfoList){
        HashMap hashMap =new HashMap();
//        laboratoryInfo.setLabid(UUID.randomUUID().toString().replaceAll("-",""));
        int i = laboratoryInfoDao.addLaboratoryInfoList(laboratoryInfoList);
        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功！！");
            hashMap.put("num",i);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","添加失败！！");
        }
        return hashMap;
    }

    //批量删除实验室
    @Override
    public HashMap delLaboratoryInfoList(String[] ids){
        HashMap hashMap = new HashMap();
        int i = laboratoryInfoDao.delLaboratoryInfoList(ids);
        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","删除成功");
            hashMap.put("num",i);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","删除失败！！");
//            hashMap.put("num",i);
        }
        return hashMap;
    }

    //更新实验室信息
    @Override
    public HashMap upLaboratoryInfo(LaboratoryInfo laboratoryInfo){
        HashMap hashMap = new HashMap();
        int i = laboratoryInfoDao.upLaboratoryInfo(laboratoryInfo);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","修改成功！！");
            return hashMap;
        }else {
            hashMap.put("status","201");
            hashMap.put("msg","修改失败！！");
            return hashMap;
        }
    }

    //更新实验室状态
    @Override
    public HashMap upStatus(String labid ,String status){
        HashMap hashMap = new HashMap();
        int i = laboratoryInfoDao.upStatus(labid, status);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","实验室已"+status);
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg",status+"失败");
            return hashMap;
        }

    }


    //取消预约
    @Override
    public HashMap delReserveAdmin(String id){
        HashMap hashMap = new HashMap();
        int i = reserveDao.delReserveAdmin(id);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","取消成功！！！");
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","取消失败！！！");
            return hashMap;
        }

    }

    //预约详情
    @Override
    public Reserve getDetailReserve(String id){
        Reserve reserve = reserveDao.getDetailReserveAdmin(id);
        return reserve;
    }

    //批量删除预约记录
    @Override
    public HashMap delReserveInfoList(String[] ids){
        HashMap hashMap = new HashMap();
        int i = reserveDao.delReserveInfoList(ids);
        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","删除成功！！");
            hashMap.put("num",i);
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","删除失败");
            return hashMap;
        }
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

    //
    @Override
    public String getLaboratoryId(LaboratoryInfo laboratoryInfo){
        String labid = laboratoryInfoDao.getLabid(laboratoryInfo);
        return labid;
    }

    //实验室物品列表
    @Override
    public List<LaboratoryThing> getLaboratoryThingList(LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo){
        List<LaboratoryThing> laboratoryThingList = laboratoryThingDao.getLaboratoryThingList(laboratoryThing,laboratoryInfo);
        return laboratoryThingList;
    }


    //添加物品
    @Override
    public HashMap addLabThing(LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo){
        HashMap hashMap = new HashMap();
        String labid = laboratoryInfoDao.getLabid(laboratoryInfo);
        System.out.println(labid+"sssssssss");
        laboratoryThing.setLabid(labid);
        laboratoryThing.setId(UUID.randomUUID().toString().replaceAll("-",""));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        laboratoryThing.setTime(df.format(new Date()));
        int i = laboratoryThingDao.addLabThing(laboratoryThing);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功");
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","添加失败");
            return hashMap;
        }
    }


    //批量删除物品
    @Override
    public HashMap delLaboratoryThingList(String[] ids){
        HashMap hashMap = new HashMap();
        int i = laboratoryThingDao.delLabThingList(ids);
        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","删除成功 ");
            hashMap.put("num",i);
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","删除失败");
            return hashMap;
        }

    }


    //批量表格添加物品
    @Override
    public HashMap addLaboratoryThingList(List<LaboratoryThing> laboratoryThingList){
        HashMap hashMap = new HashMap();
        int i = laboratoryThingDao.addLaboratoryThingList(laboratoryThingList);
        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功 ");
            hashMap.put("num",i);
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","添加失败");
            return hashMap;
        }
    }

    //获取物品详情信息
    @Override
    public LaboratoryThing getLaboratoryThing(String id){
        LaboratoryThing laboratoryThing = laboratoryThingDao.getLaboratoryThing(id);
        return laboratoryThing;
    }

    //修改保存物品信息
    @Override
   public HashMap upLaboratoryThing(LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo){
        HashMap hashMap = new HashMap();
        String labid = laboratoryInfoDao.getLabid(laboratoryInfo);
        if (labid ==null || labid =="" ){
            hashMap.put("status","201");
            hashMap.put("msg","实验室不存在，请核对正确！！");
            return hashMap;
        }
        int i = laboratoryThingDao.upLaboratoryThing(laboratoryThing);
        if(i==1){
            hashMap.put("status","200");
            hashMap.put("msg","修改成功！！");
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","修改失败！！");
            return hashMap;
        }
    }
}
