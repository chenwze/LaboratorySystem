package com.gdufe.laboratorysystem.controller;

import com.gdufe.laboratorysystem.entity.Family;
import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.Reserve;
import com.gdufe.laboratorysystem.entity.User;
import com.gdufe.laboratorysystem.service.AdminService;
import com.gdufe.laboratorysystem.utils.ExeImport;
import com.gdufe.laboratorysystem.utils.ImportExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.server.UID;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: LaboratorySystem
 * @description: 管理员路径
 * @author: chen weize
 * @create: 2022-03-23 00:09
 * @version: 1.0
 */
@Controller()
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Value("${system.user.password.secret}")
    private String secret;
    /**
     * 打开发布公告页面
     */
    @RequestMapping("/noticePage")
    public String noticePage(){
        System.out.println("------------");
        return "/admin/notice_page";
    }


    /**
     * 添加公告
     */
    @PostMapping("/addNotice")
    @ResponseBody
    public Notice addNotice(Notice notice){
        System.out.println(notice.toString());
        notice = adminService.addNotice(notice);
        if (null != notice){
            return notice;
        }else {
            return null;
        }

    }

    /**
     * 公告列表
     * @param notice
     * @return
     */
    @GetMapping("/getnoticeList")
    public String getnoticeList(Notice notice, Model model,
                                @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                @RequestParam(defaultValue="8",value="pageSize")Integer pageSize){
        System.out.println(notice.toString()+"公告列表");

        if (!"null".equals(notice.getTitle())) {
            model.addAttribute("title",notice.getTitle());
        }
        if(!"null".equals(notice.getAdminUsername())){
            model.addAttribute("adminUsername", notice.getAdminUsername());
        }
        if (!"null".equals(notice.getTime())){
            model.addAttribute("time",notice.getTime());
        }
//        System.out.println(studentService.getReserveList(reserve));

        //为了程序的严谨性，判断非空：
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 5;    //设置默认每页显示的数据数
        }
        System.out.println("当前页是："+pageNum+"显示条数是："+pageSize);

        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum,pageSize);

        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        try {
//            System.out.println(studentService.getLaboratoryInfoList(laboratoryInfo).toArray().toString());
            List<Notice> noticeList = adminService.getNoticeList(notice);
            System.out.println("分页数据："+noticeList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(noticeList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/admin/notice_list_page";
    }


    /**
     * 公告详情页
     */
    @RequestMapping("/detailsNoticePage")
    public String detailsNoticePage(Model model,Notice notice){

        System.out.println(notice.toString());
        notice = adminService.getdetailsNoticePage(notice);
        model.addAttribute("notice",notice);
        return "/admin/details_notice_page";
    }

    /**
     * 批量删除公告
     */
    @PostMapping("/delNoticeAll")
    @ResponseBody
    public Map delNoticeAll( String[] dataList){
        System.out.println(dataList[0]);
        System.out.println("sssssssssss"+dataList.length);
        int i = adminService.delNoticeList(dataList);
        System.out.println(i+"----------------");
        Map map = new HashMap();
        map.put("num",i);
        return map;
    }

    /**
     * 学生账号信息列表
     */

    @RequestMapping("/studentUserList")
    public String getStudentUserList(User user, Model model,
                                     @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                     @RequestParam(defaultValue="8",value="pageSize")Integer pageSize) {

        if (!"null".equals(user.getUsername())) {
            model.addAttribute("username",user.getUsername());
        }
        if(!"null".equals(user.getName())){
            model.addAttribute("name", user.getName());
        }
        if (!"null".equals(user.getCreateTime())){
            model.addAttribute("createTime",user.getCreateTime());
        }
//        System.out.println(studentService.getReserveList(reserve));

        //为了程序的严谨性，判断非空：
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 5;    //设置默认每页显示的数据数
        }
        System.out.println("当前页是："+pageNum+"显示条数是："+pageSize);

        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum,pageSize);

        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        try {
//            System.out.println(studentService.getLaboratoryInfoList(laboratoryInfo).toArray().toString());
            List<User> studentUserList = adminService.getStudentUserList(user);
            System.out.println("分页数据："+studentUserList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(studentUserList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/admin/student_user_list";
    }

    /**
     * 表格上传接收表格数据添加数据库
     *
     */
    @PostMapping("/addStudentUserExe")
    @ResponseBody
    public Map addStudentUserExe(MultipartFile file) throws Exception {
        InputStream in =null;
        List<List<Object>> listob = null;

        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();
        List<Object> loTitle = listob.get(1);
        List<Object> lo =null;
        List<User> userList= new ArrayList<User>();
        //调用service相应方法进行数据保存到数据库中，现只对数据输出

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        for (int i = 1; i < listob.size(); i++) {
            lo=listob.get(i);
            User user = new User();
//            user.setUserid(String.valueOf(lo.get(0)));
            user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
            user.setUsername(String.valueOf(lo.get(1)));
            user.setName(String.valueOf(lo.get(2)));
            user.setPassword(encoder.encode(String.valueOf(lo.get(3))));
            user.setRole(String.valueOf(lo.get(4)));
            user.setStatus(String.valueOf(lo.get(5)));
            user.setEmail(String.valueOf(lo.get(6)));
            user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            System.out.println("打印信息-->"+user.toString());
            userList.add(user);
//            System.out.println(userList.get(i));
        }
        int i = adminService.addStudentUserList(userList);
        System.out.println(i+"ssssssssssssssssssssssssss");
//        for (User arrayList:userList){
//            System.out.println(arrayList.toString());
//        }
        HashMap map = new HashMap();
        map.put("num",i);
        return map;

    }


    /**
     * 添加学生页面
     */
    @RequestMapping("/studentUserPage")
    public String studentUserPage(){
        return "/admin/student_user_page";
    }

    /**
     * 添加学生用户
     */
    @RequestMapping("/addStudentUser")
    @ResponseBody
    public String addStudentUser(User user){
        System.out.println(user.toString());
        user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
        user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        user.setPassword(encoder.encode(user.getPassword()));
         int i = adminService.addStudentUser(user);
        return null;
    }

    /**
     * 批量删除学生用户
     */
    @PostMapping("/delStudentUserList")
    @ResponseBody
    public Map delStudentUserList( String[] dataList){
        System.out.println(dataList[0]);
        System.out.println("sssssssssss"+dataList.length);
        int i = adminService.delStudentUserList(dataList);
        System.out.println(i+"----------------");
        Map map = new HashMap();
        map.put("num",i);
        return map;
    }



    /**
     * 老师账号信息列表
     */

    @RequestMapping("/teacherUserList")
    public String getTeacherUserList(User user, Model model,
                                     @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                     @RequestParam(defaultValue="8",value="pageSize")Integer pageSize) {

        if (!"null".equals(user.getUsername())) {
            model.addAttribute("username",user.getUsername());
        }
        if(!"null".equals(user.getName())){
            model.addAttribute("name", user.getName());
        }
        if (!"null".equals(user.getCreateTime())){
            model.addAttribute("createTime",user.getCreateTime());
        }
//        System.out.println(teacherService.getReserveList(reserve));

        //为了程序的严谨性，判断非空：
        if(pageNum == null){
            pageNum = 1;   //设置默认当前页
        }
        if(pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 5;    //设置默认每页显示的数据数
        }
        System.out.println("当前页是："+pageNum+"显示条数是："+pageSize);

        //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
        PageHelper.startPage(pageNum,pageSize);

        //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
        try {
//            System.out.println(teacherService.getLaboratoryInfoList(laboratoryInfo).toArray().toString());
            List<User> teacherUserList = adminService.getTeacherUserList(user);
            System.out.println("分页数据："+teacherUserList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(teacherUserList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/admin/teacher_user_list";
    }

    /**
     * 表格上传接收表格数据添加数据库
     *
     */
    @PostMapping("/addTeacherUserExe")
    @ResponseBody
    public Map addTeacherUserExe(MultipartFile file) throws Exception {
        InputStream in =null;
        List<List<Object>> listob = null;

        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();
        List<Object> loTitle = listob.get(1);
        List<Object> lo =null;
        List<User> userList= new ArrayList<User>();
        //调用service相应方法进行数据保存到数据库中，现只对数据输出

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        for (int i = 1; i < listob.size(); i++) {
            lo=listob.get(i);
            User user = new User();
//            user.setUserid(String.valueOf(lo.get(0)));
            user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
            user.setUsername(String.valueOf(lo.get(1)));
            user.setName(String.valueOf(lo.get(2)));
            user.setPassword(encoder.encode(String.valueOf(lo.get(3))));
            user.setRole(String.valueOf(lo.get(4)));
            user.setStatus(String.valueOf(lo.get(5)));
            user.setEmail(String.valueOf(lo.get(6)));
            user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            System.out.println("打印信息-->"+user.toString());
            userList.add(user);
//            System.out.println(userList.get(i));
        }
        int i = adminService.addTeacherUserList(userList);
        System.out.println(i+"ssssssssssssssssssssssssss");
//        for (User arrayList:userList){
//            System.out.println(arrayList.toString());
//        }
        HashMap map = new HashMap();
        map.put("num",i);
        return map;

    }


    /**
     * 添加老师页面
     */
    @RequestMapping("/teacherUserPage")
    public String teacherUserPage(){
        return "/admin/teacher_user_page";
    }

    /**
     * 添加老师用户
     */
    @RequestMapping("/addTeacherUser")
    @ResponseBody
    public String addTeacherUser(User user){
        System.out.println(user.toString());
        user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
        user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        user.setPassword(encoder.encode(user.getPassword()));
        int i = adminService.addTeacherUser(user);
        return null;
    }

    /**
     * 批量删除老师用户
     */
    @PostMapping("/delTeacherUserList")
    @ResponseBody
    public Map delTeacherUserList( String[] dataList){
        System.out.println(dataList[0]);
        System.out.println("sssssssssss"+dataList.length);
        int i = adminService.delTeacherUserList(dataList);
        System.out.println(i+"----------------");
        Map map = new HashMap();
        map.put("num",i);
        return map;
    }
}
