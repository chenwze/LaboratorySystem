package com.gdufe.laboratorysystem.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gdufe.laboratorysystem.dao.LaboratoryThingDao;
import com.gdufe.laboratorysystem.entity.*;
import com.gdufe.laboratorysystem.service.AdminService;
//import com.gdufe.laboratorysystem.utils.ExportExcel;
import com.gdufe.laboratorysystem.utils.ImgHeadUtils;
import com.gdufe.laboratorysystem.utils.ImportExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
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
    public HashMap addNotice(@Valid Notice notice, BindingResult bindingResult) throws Exception{
        System.out.println(notice.toString());
        HashMap hashMap =new HashMap();
        if( bindingResult.hasFieldErrors()){
            hashMap.put("msg",bindingResult.getFieldError().getDefaultMessage());
            hashMap.put("status","201");
            return hashMap;
        }
        notice = adminService.addNotice(notice);
        if (null != notice){
            hashMap.put("msg","发布成功！！");
            hashMap.put("status","201");
            return hashMap;
        }else {
            hashMap.put("msg","添加失败！！");
            hashMap.put("status","201");
            return hashMap;
        }

    }

    /**
     * 修改公告
     */
    @PostMapping("/upNotice")
    @ResponseBody
    public HashMap upNotice(Notice notice){
        System.out.println(notice.toString());
        HashMap hashMap = new HashMap();
        int i= adminService.upNotice(notice);
        if (null != notice){
//            return notice;
            hashMap.put("status","200");
            hashMap.put("msg","发布成功！！！");
        }else {
            hashMap.put("status","201");
            hashMap.put("msg","发布失败！！！");
        }
        return hashMap;
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
        if (notice.getDisplay() !=null && !"null".equals(notice.getDisplay())){
            model.addAttribute("display",notice.getDisplay());
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
     * 编辑公告详情页
     */
    @RequestMapping("/upNoticePage")
    public String upNoticePage(Model model,Notice notice){
        System.out.println(notice.toString());
        notice = adminService.getdetailsNoticePage(notice);
        model.addAttribute("notice",notice);
        return "/admin/notice_edit";
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
     * 隐藏公告
     */
    @RequestMapping(value = "/hideNotice",method = RequestMethod.POST)
    @ResponseBody
    public HashMap hideNotice(String id){
        Notice notice =new Notice();
        //1:表示显示，2：表示隐藏
        notice.setDisplay("0");
        notice.setId(id);
        HashMap hashMap = adminService.upNoticeStatus(notice);
        return hashMap;
    }

    /**
     * 显示公告
     */
    @RequestMapping(value = "/showNotice",method = RequestMethod.POST)
    @ResponseBody
    public HashMap showNotice(String id){
        Notice notice =new Notice();
        //1:表示显示，0：表示隐藏
        notice.setDisplay("1");
        notice.setId(id);
        HashMap hashMap = adminService.upNoticeStatus(notice);
        return hashMap;
    }

    /**
     * 查看管理员账号信息
     */
    @RequestMapping("/getAdminUser")
    public String getAdminUser(Model model){
        System.out.println("AdminController.getAdminUser");
        User adminUser = adminService.getAdminUser();
        System.out.println("adminUser = " + adminUser.toString());
        model.addAttribute("adminUser",adminUser);
        return "/admin/admin_user";
    }

    /**
     * 修改管理员账号信息页面
     */
    @RequestMapping("/changeUserInfo")
    public String changeUserInfo(Model model){
        System.out.println("AdminController.changeUserInfo");
        User adminUser = adminService.getAdminUser();
        model.addAttribute("adminUser",adminUser);
        String staticPath = this.getClass().getClassLoader().getResource("static").getFile();
        System.out.println("staticPath = " + staticPath);
        return "/admin/admin_user_edit";
    }

    /**
     * 修改保存管理账号信息
     */
    @RequestMapping(value = "/upUserInfo" , method = RequestMethod.POST )
    @ResponseBody
    public Map upUserInfo(MultipartFile file,User user){
        System.out.println("user.getName() = " + user.getName());
//        String s = ImgHeadUtils.imgHead(file);
//        System.out.println("s = " + s);
//        map.put("sss","Sss");
        HashMap hashMap = adminService.upAdminUserIfo(file, user);
        return hashMap;
//

    }


    /**
     * 修改邮箱页面
     *
     */
    @RequestMapping("/changeEmailPage")
    public String changeEmailPage(){
        System.out.println("AdminController.changeEmailPage");
        return "/admin/change_email_page";
    }
    /**
     * 修改当前账号密码页面
     */
    @RequestMapping("/changePassword")
    public String changePasswordPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            model.addAttribute("username",currentUserName);
        }
        return "/admin/change_password";
    }

//    //校验密码是否与旧密码一致
//    @RequestMapping("/checkPassword")
//    @ResponseBody
//    public HashMap checkPassword(HashMap hashMap,String newPasword){
//
//        return hashMap;
//    }

    /**
     * 修改管理员密码
     */
    @RequestMapping("/upAdminPassword")
    @ResponseBody
    public HashMap upAdminPassword(HashMap map,String oldPassword,String newPassword){
        map = adminService.upAdminPassword(oldPassword, newPassword);
        return map;
    }
    /**
     * 修改管理员账号信息页面
     */
//    @RequestMapping("/adminUserPage")
//    public String adminUserPage(Model model){
//        User adminUser = adminService.getAdminUser();
//        model.addAttribute("adminUser",adminUser);
//        return "/admin/admin_user_edit";
//    }
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
        return "/admin/student/student_user_list";
    }

    /**
     * 学生账号详情信息
     */
    @RequestMapping("/detailsStudentUser")
    public String detailsStudentUser(String username,Model model){
        User studentUser = adminService.getStudentUser(username);
        model.addAttribute("studentUser",studentUser);
//        System.out.println("studentUser.toString() = " + studentUser.toString());
        return "/admin/student/student_user_detail";
    }

    /**
     * 编辑学生账号信息
     * @param username
     * @param model
     * @return
     */
    @RequestMapping("/editStudentUser")
    public String editStudentUser(String username, Model model){
        User studentUser = adminService.getStudentUser(username);
        model.addAttribute("studentUser",studentUser);
        return "/admin/student/student_user_edit";
    }

    /**
     * 修改学生账号信息
     * @param file
     * @param user
     * @return
     */
    @RequestMapping(value = "/upStudentUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public HashMap upStudentUserInfo(MultipartFile file,User user){
        System.out.println("user = " + user.toString()+file);
        HashMap hashMap =new HashMap();

        hashMap = adminService.upStudentUserInfo(file, user);
        System.out.println("------------");
        return hashMap;
    }

    /**
     * 学生个人详情信息
     * @param username
     * @return
     */
    @RequestMapping("/detailsStudentInfoPage")
    public String detailsStudentInfoPage(String username,Model model){
        System.out.println("username = ++++++++++++++++++++++++++++++" );
        System.out.println("username = " + username);
        StudentInfo studentInfo = adminService.getStudentInfo(username);
        model.addAttribute("studentInfo",studentInfo);
        return "/admin/student/student_info_page";
    }



    /**
     * 表格上传接收表格数据添加学生账号
     *
     */
    @PostMapping("/addStudentUserExe")
    @ResponseBody
    public Map addStudentUserExe(MultipartFile file) throws Exception {
        InputStream in =null;
        System.out.println(file.getInputStream()+"sssssss");
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
        for (int i = 2; i < listob.size(); i++) {
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
//            System.out.println("打印信息-->"+user.toString());
            userList.add(user);
//            System.out.println(userList.get(i));
        }
        int i = adminService.addStudentUserList(userList);
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
        return "/admin/student/student_user_page";
    }

    /**
     * 添加学生用户
     */
    @RequestMapping("/addStudentUser")
    @ResponseBody
    public HashMap addStudentUser(User user){
        System.out.println(user.toString());
        HashMap hashMap = new HashMap();
        user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
        user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("student");
         int i = adminService.addStudentUser(user);
         if (i==10){
             hashMap.put("status","201");
             hashMap.put("msg","邮箱已存在！！");
             return hashMap;
         }

         if (i==1){
             hashMap.put("status","200");
             hashMap.put("msg","添加成功！！");
             return hashMap;
         }
        hashMap.put("status","201");
        hashMap.put("msg","未知错误！！");
        return hashMap;
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
     * 重置学生账号密码
     */
    @RequestMapping("/resetStudentPasssword")
    @ResponseBody
    public HashMap resetStudentPasssword( String username){
        System.out.println(username);
        HashMap hashMap = adminService.resetStudentPasssword(username);
        return hashMap;
    }

    /**
     * 学生个人信息列表
     */
    @RequestMapping("/getStudentInfoList")
    public String getStudentInfoList(StudentInfo studentInfo, Model model,
                                     @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                     @RequestParam(defaultValue="8",value="pageSize")Integer pageSize) {

        if (!"null".equals(studentInfo.getUsername())) {
            model.addAttribute("username",studentInfo.getUsername());
        }
        if(!"null".equals(studentInfo.getName())){
            model.addAttribute("name", studentInfo.getName());
        }
        if(!"null".equals(studentInfo.getMajor())){
            model.addAttribute("major", studentInfo.getMajor());
        }
//        if (!"null".equals(studentInfo.getCreateTime())){
//            model.addAttribute("createTime",studentInfo.getCreateTime());
//        }
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
            List<StudentInfo> studentInfoList = adminService.getStudentInfoList(studentInfo);
            System.out.println("分页数据："+studentInfoList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(studentInfoList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/admin/student/student_info_list";
    }

    //上传表格添加学生个人信息
    @RequestMapping("/addStudentInfoExe")
    @ResponseBody
    public HashMap addStudentInfoList(MultipartFile file) throws Exception {
        HashMap hashMap =new HashMap();
        InputStream in =null;
        List<List<Object>> listob = null;

        if(file.isEmpty()){
//            throw new Exception("文件不存在！");
            hashMap.put("status","201");
            hashMap.put("msg","文件不存在!");
            return hashMap;
        }

        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();
        List<Object> loTitle = listob.get(1);
        List<Object> lo =null;
        List<StudentInfo> studentInfoList= new ArrayList<StudentInfo>();
        //调用service相应方法进行数据保存到数据库中，现只对数据输出

//        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        for (int i = 2; i < listob.size(); i++) {
            lo=listob.get(i);
            StudentInfo studentInfo = new StudentInfo();
//            user.setUserid(String.valueOf(lo.get(0)));
//            user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
            studentInfo.setUsername(String.valueOf(lo.get(0)));
            studentInfo.setName(String.valueOf(lo.get(1)));
            studentInfo.setAge(Integer.valueOf(String.valueOf(lo.get(2))).intValue());
            studentInfo.setSex(String.valueOf(lo.get(3)).charAt(0));
            studentInfo.setTel(String.valueOf(lo.get(4)));
            studentInfo.setStudayData(String.valueOf(lo.get(5)));
            studentInfo.setGraduationDate(String.valueOf(lo.get(6)));
            studentInfo.setAddress(String.valueOf(lo.get(7)));
            studentInfo.setCollege(String.valueOf(lo.get(8)));
            studentInfo.setMajor(String.valueOf(lo.get(9)));
            studentInfo.setAclass(String.valueOf(lo.get(10)));
            studentInfo.setBirth(String.valueOf(lo.get(11)));
//            System.out.println(studentInfo.toString()+"ssss");
            studentInfoList.add(studentInfo);
        }
        int i = adminService.addStudentInfoList(studentInfoList);
        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功");
            hashMap.put("num",i);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","添加失败");
//            hashMap.put("num",i);
        }
        return hashMap;
    }

    //删除学生个人信息
    @RequestMapping("/delStudentInfoList")
    @ResponseBody
    public HashMap delStudentInfoList(String[] ids){
        HashMap hashMap = new HashMap();
        int i = adminService.delStudentInfoList(ids);
        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","删除成功");
            hashMap.put("num",i);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","删除失败");
            hashMap.put("num",0);
        }
        return hashMap;
    }

    /**
     * 学生信息添加页面/编辑页面
     */
    @RequestMapping(value = {"/addStudentInfoPage","/editStudentInfo"})
    public String addStudentInfoPage(String username,Model model){
        System.out.println(username+"++++");
        if(username != null && !username.equals("null") && !username.equals("")){
            StudentInfo studentInfo = adminService.getStudentInfo(username);
            model.addAttribute("studentInfo",studentInfo);
            return "/admin/student/student_info_edit";
        }else{
            return "/admin/student/student_info_add";
        }
    }

    /**
     * 添加学生个人信息
     */
    @RequestMapping("/addStudentInfo")
    @ResponseBody
    public HashMap addStudentInfo(StudentInfo studentInfo){
        System.out.println(studentInfo.toString());
        HashMap hashMap = new HashMap();
        hashMap = adminService.addStudentInfo(studentInfo);
        return hashMap;
    }

    /**
     * 修改保存学生个人信息
     */
    @RequestMapping("/upStudentInfo")
    @ResponseBody
    public HashMap upStudentInfo(StudentInfo studentInfo){
        System.out.println(studentInfo.toString()+studentInfo.getAge()+studentInfo.getSex());
        HashMap hashMap = new HashMap();
        hashMap = adminService.upStudentInfo(studentInfo);
        return hashMap;
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
        return "/admin/teacher/teacher_user_list";
    }

    /**
     * 老师表格上传接收表格老师账号数据添加数据库
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
        for (int i = 2; i < listob.size(); i++) {
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
//            System.out.println("打印信息-->"+user.toString());
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
        return "/admin/teacher/teacher_user_page";
    }

    /**
     * 添加老师用户
     */
    @RequestMapping("/addTeacherUser")
    @ResponseBody
    public HashMap addTeacherUser(User user){
        HashMap hashMap = new HashMap();
        System.out.println(user.toString());
        user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
        user.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("teacher");
        int i = adminService.addTeacherUser(user);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功！！");
            return hashMap;
        }else if (i==10){
            hashMap.put("status","201");
            hashMap.put("msg","邮箱已存在！！");
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","添加失败，请稍后重试！！");
            return hashMap;
        }
//        return null;
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

    /**
     * 重置教师账号密码
     */
    @RequestMapping("/resetTeacherPasssword")
    @ResponseBody
    public HashMap resetTeacherPasssword( String username){
        System.out.println(username);
        HashMap hashMap = adminService.resetTeacherPasssword(username);
        return hashMap;
    }

    /**
     *编辑老师页面
     */
    @RequestMapping("/editTeacherUser")
    public String editTeacherUser(String username,Model model){
        User teacherUser = adminService.getTeacherUser(username);
        model.addAttribute("teacherUser",teacherUser);
        return "/admin/teacher/teacher_user_edit";
    }

    /**
     * 修改老师账号信息
     * @param file
     * @param user
     * @return
     */
    @RequestMapping(value = "/upTeacherUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public HashMap upTeacherUserInfo(MultipartFile file,User user){
        System.out.println("user = " + user.toString()+file);
        HashMap hashMap =new HashMap();

        hashMap = adminService.upTeacherUserInfo(file, user);
        System.out.println("------------");
        return hashMap;
    }

    /**
     * 老师账号详情页面
     */
    @RequestMapping("/detailsTeacherUser")
    public String detailsTeacherUser(String username,Model model){
        User teacherUser = adminService.getTeacherUser(username);
        model.addAttribute("teacherUser",teacherUser);
        return "/admin/teacher/teacher_user_detail";
    }

    /**
     * 老师个人信息列表
     */
    @RequestMapping("/getTeacherInfoList")
    public String getTeacherInfoList(TeacherInfo teacherInfo, Model model,
                                     @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                     @RequestParam(defaultValue="8",value="pageSize")Integer pageSize) {

        if (!"null".equals(teacherInfo.getUsername())) {
            model.addAttribute("username",teacherInfo.getUsername());
        }
        if(!"null".equals(teacherInfo.getName())){
            model.addAttribute("name", teacherInfo.getName());
        }
//        if (!"null".equals(studentInfo.getCreateTime())){
//            model.addAttribute("createTime",studentInfo.getCreateTime());
//        }
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
            List<TeacherInfo> teacherInfoList = adminService.getTeacherInfoList(teacherInfo);
            System.out.println("分页数据："+teacherInfoList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(teacherInfoList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/admin/teacher/teacher_info_list";
    }

    /**
     * 老师个人详情信息
     * @param username
     * @return
     */
    @RequestMapping("/detailsTeacherInfoPage")
    public String detailsTeacherInfoPage(String username,Model model){
        System.out.println("username = ++++++++++++++++++++++++++++++" );
        System.out.println("username = " + username);
        TeacherInfo teacherInfo = adminService.getTeacherInfo(username);
        if (teacherInfo==null){
            return "error/500";
        }
        model.addAttribute("teacherInfo",teacherInfo);
        return "/admin/teacher/teacher_info_page";
    }

    //添加老师个人信息页面
    @RequestMapping("/addTeacherInfoPage")
    public String addTeacherInfoPage(){
        return "/admin/teacher/teacher_info_add";
    }

    //编辑老师个人 信息页
    @RequestMapping("/editTeacherInfoPage")
    public String editTeacherInfoPage(String username,Model model){
        TeacherInfo teacherInfo = adminService.getTeacherInfo(username);
        if (teacherInfo == null){
            return null;
        }
        model.addAttribute("teacherInfo",teacherInfo);
        return "/admin/teacher/teacher_info_edit";
    }

    /**
     * 添加老师个人信息
     * @return
     */
    @RequestMapping("/addTeacherInfo")
    @ResponseBody
    public HashMap addTeacherInfo(TeacherInfo teacherInfo){
        HashMap hashMap = new HashMap();
        System.out.println(teacherInfo.toString()+"suername");
        hashMap=adminService.addTeacherInfo(teacherInfo);
        return hashMap;
    }

    /**
     * 编辑保存老师个人信息
     * @return
     */
    @RequestMapping("/upTeacherInfo")
    @ResponseBody
    public HashMap upTeacherInfo(TeacherInfo teacherInfo){
        HashMap hashMap = new HashMap();
        System.out.println(teacherInfo.toString()+"suername");
        hashMap=adminService.upTeacherInfo(teacherInfo);
        return hashMap;
    }

    //表格批量添加老师个人信息
    @RequestMapping("/addTeacherInfoList")
    @ResponseBody
    public  HashMap addTeacherInfoList(MultipartFile file) throws Exception {
        HashMap hashMap = new HashMap();
        InputStream in =null;
        System.out.println(file.getInputStream()+"sssssss");
        List<List<Object>> listob = null;

        if(file != null &&file.isEmpty()) {
//            throw new Exception("文件不存在！");
            hashMap.put("status","201");
            hashMap.put("msg","文件不存在!");
            return hashMap;
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();
        List<Object> loTitle = listob.get(1);
        List<Object> lo =null;
        List<TeacherInfo> teacherInfoList= new ArrayList<TeacherInfo>();

        //调用service相应方法进行数据保存到数据库中，现只对数据输出
       for (int i = 2; i < listob.size(); i++) {
            lo=listob.get(i);
            TeacherInfo teacherInfo = new TeacherInfo();
//            user.setUserid(String.valueOf(lo.get(0)));
//            user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
           teacherInfo.setUsername(String.valueOf(lo.get(0)));
           teacherInfo.setName(String.valueOf(lo.get(1)));
           teacherInfo.setAge(Integer.valueOf(String.valueOf(lo.get(2))).intValue());
           teacherInfo.setSex(String.valueOf(lo.get(3)).charAt(0));
           teacherInfo.setTel(String.valueOf(lo.get(4)));
           teacherInfo.setEntryDate(String.valueOf(lo.get(5)));
           teacherInfo.setResignationDate(String.valueOf(lo.get(6)));
           teacherInfo.setAddress(String.valueOf(lo.get(7)));
           teacherInfo.setCollege(String.valueOf(lo.get(8)));
//           teacherInfo.setMajor(String.valueOf(lo.get(9)));
           teacherInfo.setPosition(String.valueOf(lo.get(9)));
           teacherInfo.setBirth(String.valueOf(lo.get(10)));
//            System.out.println(studentInfo.toString()+"ssss");
           teacherInfoList.add(teacherInfo);
        }
        int i = adminService.addTeacherInfoList(teacherInfoList);
//        for (User arrayList:userList){
//            System.out.println(arrayList.toString());
//        }

        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功");
            hashMap.put("num",i);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","添加失败");
//            hashMap.put("num",i);
        }
        return hashMap;
    }

    //批量删除老师个人信息
    @RequestMapping("/delTeacherInfoList")
    @ResponseBody
    public HashMap delTeacherInfoList(String[] ids){
        HashMap hashMap = new HashMap();
        int i = adminService.delTeacherInfoList(ids);
        if(i>0){
            hashMap.put("status","200");
            hashMap.put("msg","删除成功");
            hashMap.put("num",i);
        }else {
            hashMap.put("status","201");
            hashMap.put("msg","删除失败！！");
        }
        return hashMap;
    }


    /**
     * 实验室列表
     */
    @RequestMapping("/getLaboratoryInfoList")
    public String getLaboratoryInfoList(LaboratoryThing laboratoryThing,Model model, LaboratoryInfo laboratoryInfo,
                                        String reserve,
                                        @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                        @RequestParam(defaultValue="8",value="pageSize")Integer pageSize){

        System.out.println(laboratoryInfo.toString());
        if (!"null".equals(laboratoryInfo.getBuildingName())) {
            model.addAttribute("buildingName",laboratoryInfo.getBuildingName());
        }
//        if(!"null".equals(reserve)){
//            model.addAttribute("reserve",reserve);
//        }
        if (!"null".equals(laboratoryInfo.getCategory())){
            model.addAttribute("category",laboratoryInfo.getCategory());
        }
        if (!"null".equals(laboratoryInfo.getRoomNumber())){
            model.addAttribute("roomNumber",laboratoryInfo.getRoomNumber());
        }
        if (!"null".equals(laboratoryInfo.getStatus())){
            model.addAttribute("status",laboratoryInfo.getStatus());
        }

        System.out.println(reserve+"+++++++++++++");

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
            List<LaboratoryInfo> laboratoryInfos = adminService.getLaboratoryInfoList(laboratoryInfo,reserve);
            System.out.println("分页数据："+laboratoryInfos);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(laboratoryInfos);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/admin/laboratory_info_list";
    }

    /**
     * 实验室预约记录
     */
    @RequestMapping("/getAdminReserveList")
    public String getAdminReserveList(Model model, LaboratoryInfo laboratoryInfo, Reserve reserve,
                                      @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                      @RequestParam(defaultValue="8",value="pageSize")Integer pageSize){

        System.out.println(laboratoryInfo.toString()+reserve.toString()+"+++++");
        if (!"null".equals(laboratoryInfo.getBuildingName())) {
            model.addAttribute("buildingName",laboratoryInfo.getBuildingName());
        }
        if(!"null".equals(reserve.getReserveTime()) && !"".equals(reserve.getReserveTime()) && " " != reserve.getReserveTime()){
            model.addAttribute("reserveTime",reserve.getReserveTime());
        }
        if (!"null".equals(laboratoryInfo.getCategory())){
            model.addAttribute("category",laboratoryInfo.getCategory());
        }
        if(!"null".equals(reserve.getUsername()) && !"".equals(reserve.getUsername()) && " " != reserve.getUsername()){
            model.addAttribute("username",reserve.getUsername());
        }

        if(!"null".equals(reserve.getUserType()) && !"".equals(reserve.getUserType()) && " " != reserve.getUserType()){
            model.addAttribute("userType",reserve.getUserType());
        }
//        System.out.println(studentService.getReserveList(reserve));
        System.out.println("AdminController.getAdminReserveList");
        System.out.println(reserve.toString());

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
            List<Reserve> adminReserveList = adminService.getAdminReserveList(reserve, laboratoryInfo);
            System.out.println("分页数据："+adminReserveList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(adminReserveList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html

        return "/admin/reserve_list";
    }

    //添加实验室信息页面
    @RequestMapping("/addLaboratoryInfoPage")
    public String addLaboratoryInfoPage(){
        return "admin/laboratory_info_add";
    }

    //添加实验室
    @RequestMapping("/addLaboratoryInfo")
    @ResponseBody
    public HashMap addLaboratoryInfo(LaboratoryInfo laboratoryInfo){
        HashMap hashMap =new HashMap();
        hashMap = adminService.addLaboratoryInfo(laboratoryInfo);
        return hashMap;
    }

    //表格批量添加实验室
    @RequestMapping("/addLaboratoryInfoList")
    @ResponseBody
    public HashMap addLaboratoryInfoList(MultipartFile file) throws Exception {
        HashMap hashMap = new HashMap();
        InputStream in =null;
        System.out.println(file.getInputStream()+"sssssss");
        List<List<Object>> listob = null;

        if(file != null &&file.isEmpty()) {
//            throw new Exception("文件不存在！");
            hashMap.put("status","201");
            hashMap.put("msg","文件不存在!");
            return hashMap;
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();
        List<Object> loTitle = listob.get(1);
        List<Object> lo =null;
        List<LaboratoryInfo> laboratoryInfoList= new ArrayList<LaboratoryInfo>();

        //调用service相应方法进行数据保存到数据库中，现只对数据输出
        for (int i = 2; i < listob.size(); i++) {
            lo=listob.get(i);
            LaboratoryInfo laboratoryInfo = new LaboratoryInfo();
//            user.setUserid(String.valueOf(lo.get(0)));
//            user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
            laboratoryInfo.setLabid(UUID.randomUUID().toString().replaceAll("-",""));
            laboratoryInfo.setBuildingName(String.valueOf(lo.get(0)));
            laboratoryInfo.setCapacity(Integer.valueOf(String.valueOf(lo.get(1))).intValue());
            laboratoryInfo.setCategory(String.valueOf(lo.get(2)));
            laboratoryInfo.setDescribe(String.valueOf(lo.get(3)));
            laboratoryInfo.setStatus(String.valueOf(lo.get(4)));
            laboratoryInfo.setRoomNumber(String.valueOf(lo.get(5)));
//            System.out.println(studentInfo.toString()+"ssss");
            laboratoryInfoList.add(laboratoryInfo);
        }
        hashMap = adminService.addLaboratoryInfoList(laboratoryInfoList);
//        for (User arrayList:userList){
//            System.out.println(arrayList.toString());
//        }

        return hashMap;
    }

    //批量删除实验室
    @RequestMapping("/delLaboratoryInfoList")
    @ResponseBody
    public HashMap delLaboratoryInfoList(String[] ids){
        HashMap hashMap = new HashMap();
        hashMap = adminService.delLaboratoryInfoList(ids);
        return hashMap;
    }

    //实验室详情
    @RequestMapping("/detailsLaboratoryPage")
    public String detailsLaboratoryPage(String labid,Model model){
        LaboratoryInfo laboratoryInfo = adminService.getLaboratoryInfo(labid);
        if (laboratoryInfo==null){
            return "/error/500";
        }else{
            model.addAttribute("laboratoryInfo",laboratoryInfo);
            return "/admin/laboratory_info_details";
        }
    }

    //预约实验室页面
    @RequestMapping("/reserveLaboratoryPage")
    public String reserveLaboratoryPage(String labid,Model model){
        LaboratoryInfo laboratoryInfo = adminService.getLaboratoryInfo(labid);
        model.addAttribute("laboratoryInfo",laboratoryInfo);
        return "/admin/laboratory_reserve_add";
    }

    //预约实验室记录
    @RequestMapping("/addReserveLaboratoryInfo")
    @ResponseBody
    public HashMap addReserveLaboratoryInfo(LaboratoryInfo laboratoryInfo,String reserveTime){
        System.out.println(laboratoryInfo.getLabid()+"++++++"+reserveTime);
        HashMap hashMap = new HashMap();
        hashMap = adminService.addReserve(laboratoryInfo.getLabid(), reserveTime);
        return hashMap;
    }

    //编辑实验室信息页
    @RequestMapping("/editLaboratoryInfoPage")
    public String editLaboratoryInfoPage(Model model,String labid){
        LaboratoryInfo laboratoryInfo = adminService.getLaboratoryInfo(labid);
        model.addAttribute("laboratoryInfo",laboratoryInfo);
        return "/admin/laboratory_info_edit";
    }

    //修改保存实验室信息
    @RequestMapping("/upLaboratoryInfo")
    @ResponseBody
    public HashMap editLaboratoryInfoPage(LaboratoryInfo laboratoryInfo){

        HashMap hashMap = new HashMap();
        hashMap = adminService.upLaboratoryInfo(laboratoryInfo);
        return hashMap;
    }

    //开放实验室
    @RequestMapping("/openLaboratory")
    @ResponseBody
    public HashMap openLaboratory(String labid){
        System.out.println("labid = " + labid);
        HashMap hashMap = new HashMap();
        hashMap  = adminService.upStatus(labid, "开放");
        return hashMap;
    }
    //关闭实验室
    @RequestMapping("/closeLaboratory")
    @ResponseBody
    public HashMap closeLaboratory(String labid){
        System.out.println("labid = " + labid);
        HashMap hashMap = new HashMap();
        hashMap  = adminService.upStatus(labid, "关闭");
        return hashMap;
    }

    //取消预约
    @RequestMapping("/delReserve")
    @ResponseBody
    public HashMap delReserve(String id){
        HashMap hashMap = new HashMap();
        hashMap = adminService.delReserveAdmin(id);
        return hashMap;
    }

    //预约详情
    @RequestMapping("/detailReserveInfoPage")
    public String detailReserveInfoPage(String id,Model model){
        Reserve reserve = adminService.getDetailReserve(id);
        model.addAttribute("reserve",reserve);
        return "/admin/reserve_info_details";
    }

    //批量删除预约记录
    @RequestMapping("/delReserveInfoList")
    @ResponseBody
    public HashMap delReserveInfoList(String[] ids){
        HashMap hashMap = new HashMap();
        hashMap=adminService.delReserveInfoList(ids);
        return hashMap;
    }


    //验证密码是否与旧密码相同
    @RequestMapping("/verifyPasswo")
    @ResponseBody
    public HashMap verifyPassword(HashMap map,String newPassword1 ,String oldPassword){

        return map;
    }
    /**
     * 修改admin密码
     */
    @RequestMapping("/upPassword")
    @ResponseBody
    public HashMap upPassword(String newPassword1 ,String oldPassword){
        HashMap map = new HashMap();
        System.out.println("AdminController.upPassword");
        System.out.println("map = " + oldPassword + ", password = " + newPassword1);
        map = adminService.upAdminPassword(oldPassword,newPassword1);
        return map;
    }

    /**
     * 超级管理员更新密码
     */
    @RequestMapping("/superAdminUpPasswod")
    @ResponseBody
    public HashMap superAdminUpPasswod(HashMap map,String username,String password){
        System.out.println("AdminController.superAdminUpPasswod");
        System.out.println("map = " + map + ", password = " + password+username);
        map = adminService.upPassword(username,password);
        return map;
    }



    //实验室物品信息列表
    @RequestMapping("/laboratoryThingList")
    public String laboratoryThingList(Model model, LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo,
                                      @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                      @RequestParam(defaultValue="8",value="pageSize")Integer pageSize){

        System.out.println(laboratoryThing.toString()+"----------"+laboratoryInfo.toString());
        if (laboratoryThing.getType() !=null &&!"null".equals(laboratoryThing.getType())) {
            model.addAttribute("type",laboratoryThing.getType());
        }

        if (laboratoryThing.getName() !=null &&!"null".equals(laboratoryThing.getName())) {
            model.addAttribute("name",laboratoryThing.getName() );
        }

        if (laboratoryThing.getTime()!=null &&!"null".equals(laboratoryThing.getTime())) {
            model.addAttribute("time",laboratoryThing.getTime());
        }

        if (laboratoryThing.getWay() !=null &&!"null".equals(laboratoryThing.getWay())) {
            model.addAttribute("way",laboratoryThing.getWay() );
        }

        if (laboratoryInfo.getBuildingName() != null && !"null".equals(laboratoryInfo.getBuildingName())) {
            model.addAttribute("buildingName",laboratoryInfo.getBuildingName() );
        }

        if (laboratoryInfo.getRoomNumber() !=null &&!"null".equals(laboratoryInfo.getRoomNumber())) {
            model.addAttribute("roomNumber",laboratoryInfo.getRoomNumber());
        }
//        System.out.println(studentService.getReserveList(reserve));
        System.out.println("AdminController.getAdminReserveList");


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
            List<LaboratoryThing> laboratoryThingList = adminService.getLaboratoryThingList(laboratoryThing,laboratoryInfo);
            System.out.println("分页数据："+laboratoryThingList.toString()+"---------");
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(laboratoryThingList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html

        return "/admin/thing/laboratory_thing_list";
    }

    //添加物品页
    @RequestMapping("/addLabThingPage")
    public String addLabThingPage(){
        return "/admin/thing/laboratory_thing_add";
    }

    //添加物品
    @RequestMapping("/addLaboratoryThing")
    @ResponseBody
    public HashMap addLaboratoryThing(LaboratoryInfo laboratoryInfo ,LaboratoryThing laboratoryThing){
        HashMap hashMap = new HashMap();
        hashMap=adminService.addLabThing(laboratoryThing,laboratoryInfo);
        return hashMap;
    }
    
    //批量删除实验物品
    @RequestMapping("/delLaboratoryThingList")
    @ResponseBody
    public HashMap delLaboratoryThingList(String[] ids){
        HashMap hashMap = new HashMap();
        hashMap = adminService.delLaboratoryThingList(ids);
        return hashMap;
    }

    //批量添加物品
    @RequestMapping("/addLaboratoryThingList")
    @ResponseBody
    public HashMap addLaboratoryThingList(MultipartFile file) throws Exception {
        HashMap hashMap = new HashMap();
        InputStream in =null;
        System.out.println(file.getInputStream()+"sssssss");
        List<List<Object>> listob = null;

        if(file != null && file.isEmpty()) {
//            throw new Exception("文件不存在！");
            hashMap.put("status","201");
            hashMap.put("msg","文件不存在!");
            return hashMap;
        }
        in = file.getInputStream();
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());
        in.close();
        List<Object> loTitle = listob.get(1);
        List<Object> lo =null;
        List<LaboratoryThing> laboratoryThingList= new ArrayList<LaboratoryThing>();
        LaboratoryInfo laboratoryInfo = new LaboratoryInfo();
        //调用service相应方法进行数据保存到数据库中，现只对数据输出
        for (int i = 2; i < listob.size(); i++) {
            lo=listob.get(i);
            LaboratoryThing laboratoryThing = new LaboratoryThing();
//            user.setUserid(String.valueOf(lo.get(0)));
//            user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
            laboratoryInfo.setBuildingName(String.valueOf(lo.get(0)));
            laboratoryInfo.setRoomNumber(String.valueOf(lo.get(1)));
            String laboratoryId = adminService.getLaboratoryId(laboratoryInfo);
            laboratoryThing.setLabid(laboratoryId);
            laboratoryThing.setId(UUID.randomUUID().toString().replaceAll("-",""));
            laboratoryThing.setType(String.valueOf(lo.get(2)));
            laboratoryThing.setWay(String.valueOf(lo.get(3)));
            laboratoryThing.setName(String.valueOf(lo.get(4)));
            laboratoryThing.setIntroduce(String.valueOf(lo.get(5)));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            laboratoryThing.setTime(df.format(new Date()));
//            System.out.println(studentInfo.toString()+"ssss");
            laboratoryThingList.add(laboratoryThing);
        }
        hashMap = adminService.addLaboratoryThingList(laboratoryThingList);
//        for (User arrayList:userList){
//            System.out.println(arrayList.toString());
//        }

        return hashMap;
    }


    //获取物品详情信息页
    @RequestMapping("/detailsLaboratoryThingPage")
    public String detailsLaboratoryThingPage(String id,Model model){
        LaboratoryThing laboratoryThing = adminService.getLaboratoryThing(id);
        model.addAttribute("laboratoryThing",laboratoryThing);
        return "/admin/thing/laboratory_thing_details";
    }

    //编辑物品信息页
    @RequestMapping("/editLaboratoryThingPage")
    public String editLaboratoryThingPage(String id,Model model){
        LaboratoryThing laboratoryThing = adminService.getLaboratoryThing(id);
        model.addAttribute("laboratoryThing",laboratoryThing);
        return "/admin/thing/laboratory_thing_edit";
    }

    //更新保存物品信息页
    @RequestMapping("/upLaboratoryThing")
    @ResponseBody
    public HashMap upLaboratoryThing(LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo){
        HashMap hashMap = new HashMap();
        hashMap = adminService.upLaboratoryThing(laboratoryThing,laboratoryInfo);
        return hashMap;
    }


    //导出物品清单表格
    @RequestMapping("/exportLaboratoryThingTable")
    public void exportLaboratoryThingTable(LaboratoryThing laboratoryThing,LaboratoryInfo laboratoryInfo,
                                           HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("octets/stream");
        String excelName = "实验室物品清单";
        //转码防止乱码
        response.addHeader("Content-Disposition", "attachment;filename="+new String( excelName.getBytes("gb2312"), "ISO8859-1" )+".xls");
        String[] headers = new String[]{"物品编号","实验楼","实验室","物品类型","所得方式","购买者","物品介绍","入库时间"};
        System.out.println(laboratoryInfo.toString()+laboratoryThing.toString());
        List<LaboratoryThing> laboratoryThingList = adminService.getLaboratoryThingList(laboratoryThing, laboratoryInfo);

        try {
            OutputStream out = response.getOutputStream();

            //声明一个工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            //生成一个表格
            HSSFSheet sheet = workbook.createSheet(excelName);
            //设置表格默认列宽度为15个字符
            sheet.setDefaultColumnWidth(20);
            //生成一个样式，用来设置标题样式
            HSSFCellStyle style = workbook.createCellStyle();
            HSSFRow row = sheet.createRow(0);
            for(int i = 0; i<headers.length;i++){
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }
            for (int i = 0; i < laboratoryThingList.size(); i++) {
                row = sheet.createRow(i+1);
                int j = 0;
                LaboratoryThing data = laboratoryThingList.get(i);
                row.createCell(j++).setCellValue(data.getId());
                row.createCell(j++).setCellValue(data.getLaboratoryInfo().getBuildingName());
                row.createCell(j++).setCellValue(data.getLaboratoryInfo().getRoomNumber());
                row.createCell(j++).setCellValue(data.getType());
                row.createCell(j++).setCellValue(data.getWay());
                row.createCell(j++).setCellValue(data.getName());
                row.createCell(j++).setCellValue(data.getIntroduce());
                row.createCell(j++).setCellValue(data.getTime());
            }
            workbook.write(out);
            out.close();
            System.out.println("excel导出成功！"+laboratoryThingList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Map<String,Object>> getList(){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i = 0; i<100;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("number",1000+i);
            map.put("name", "张三"+i);
            int age = (int)(Math.random()*100);
            do{
                age = (int)(Math.random()*100);
            }while(age<10||age>15);
            map.put("age", age);
            map.put("sex", age%2==0?0:1);//获得随机性别
            list.add(map);
        }
        return list;
    }
}
