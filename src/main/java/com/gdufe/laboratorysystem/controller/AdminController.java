package com.gdufe.laboratorysystem.controller;

import com.gdufe.laboratorysystem.dao.LaboratoryThingDao;
import com.gdufe.laboratorysystem.entity.*;
import com.gdufe.laboratorysystem.service.AdminService;
import com.gdufe.laboratorysystem.utils.ImgHeadUtils;
import com.gdufe.laboratorysystem.utils.ImportExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * 修改公告
     */
    @PostMapping("/upNotice")
    @ResponseBody
    public Notice upNotice(Notice notice){
        System.out.println(notice.toString());
        int i= adminService.upNotice(notice);
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
     * 修改管理员信息页面
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
//        String staticPath = this.getClass().getClassLoader().getResource("static").getFile();

//        if (file != null && !file.isEmpty()){
//
//            System.out.println("AdminController.upUserInfo");
//            //获取项目classes/static的地址
//            String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
//            System.out.println("staticPath = " + staticPath);
//            String fileName = file.getOriginalFilename();  //获取文件名
//            System.out.println("fileName = " + fileName);
//            fileName=UUID.randomUUID().toString().replaceAll("-","")+fileName;
//            System.out.println("fileName = " + fileName);
//            // 图片存储目录及图片名称
//            String url_path = "imghead" + File.separator + fileName;
//            //图片保存路径
//            String savePath = staticPath + File.separator + url_path;
//            System.out.println("图片保存地址："+savePath);
//            // 访问路径=静态资源路径+文件目录路径
////            String visitPath ="static/" + url_path;
////            System.out.println("图片访问uri："+visitPath);
//
//            File saveFile = new File(savePath);
//            if (!saveFile.exists()){
//                saveFile.mkdirs();
//            }
//            try {
//                file.transferTo(saveFile);  //将临时存储的文件移动到真实存储路径下
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


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
     * 修改账号密码页面
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
        return "/admin/student/student_user_page";
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
        return "/admin/teacher/teacher_user_page";
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
        model.addAttribute("teacherInfo",teacherInfo);
        return "/admin/teacher/teacher_info_page";
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
       laboratoryThing = adminService.add(laboratoryThing);
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

        return "/admin/admin_reserve_list";
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


}
