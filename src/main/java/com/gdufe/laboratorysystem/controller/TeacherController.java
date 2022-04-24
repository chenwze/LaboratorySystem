package com.gdufe.laboratorysystem.controller;

import com.gdufe.laboratorysystem.entity.*;
import com.gdufe.laboratorysystem.service.TeacherService;
//import com.gdufe.laboratorysystem.utils.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: LaboratorySystem
 * @description: 老师权限操作
 * @author: chen weize
 * @create: 2022-03-06 12:26
 * @version: 1.0
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @RequestMapping("/teacher_index")
    public String teacherIndex(){
    return "/teacher/teacher_main";
    }

    //用户个人账号信息
    @RequestMapping("/user")
    public String userPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
//            System.out.println(UserInfo.getUser().getUsername());
            User user = teacherService.getTeacherUser(currentUserName);
            System.out.println(user.toString());
            model.addAttribute("teacherUser",user);
            return"/teacher/teacher_user";
        }else {
            return "redirect:/userLogin";
        }

    }

    /**
     * 编辑账号信息页
     * @return
     */
    @RequestMapping("/changeUserInfo")
    public String changeUserInfo(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            User teacherUser = teacherService.getTeacherUser(currentUserName);
            model.addAttribute("teacherUser", teacherUser);

            return "/teacher/teacher_user_edit";
        }else {
            return "/login";
        }

    }

    /**
     *修改保存学生账号信息
     */
    @RequestMapping("upUserInfo")
    @ResponseBody
    public HashMap upUserInfo(MultipartFile file, User user){
        System.out.println("user.getName() = " + user.getName());
//        String s = ImgHeadUtils.imgHead(file);
//        System.out.println("s = " + s);
//        map.put("sss","Sss");
        HashMap hashMap = teacherService.upTeacherUserInfo(file, user);
        return hashMap;
    }


    //查看公告
    @RequestMapping("/showNotice")
    public String noticePage(Model model){
        List<Notice> showNotice = teacherService.getShowNotice();
        for (Notice noticeList:showNotice){
            System.out.println(noticeList.toString());
        }
        model.addAttribute("notices",showNotice);
        return "/teacher/teacher_notice";
    }

    /**
     * 打开更改密码页面
     * @param model
     * @return
     */
    @RequestMapping("/changePassword")
    public String changePasswordPage(Model model){
        System.out.println("+++++++++++");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            model.addAttribute("username",currentUserName);
            return "/teacher/change_password";
        }else {
            return "redirect:/userLogin";
        }

    }

    /**
     * 更新密码
     * @param user 用户账号信息
     * @return
     */
    @PostMapping("/upPassword")
    @ResponseBody
    public  Object upPassword(User user,Model model,HttpServletRequest request){
        System.out.println("----------"+user.toString());
        //旧密码
        String oldPassword = request.getParameter("old-password");
        //新密码
        String newPassword1 = request.getParameter("new-password1");
        //确定密码
        String newPassword2 = request.getParameter("new-password2");

        System.out.println(newPassword1+"-------"+newPassword2+"++++++"+oldPassword);
        boolean bool = teacherService.upPassword(oldPassword, newPassword1);
        if (bool){
            Map map=new HashMap();
            map.put("status",200);
            map.put("user",user);
            return map;
        }else {
            Map map=new HashMap();
            map.put("status",400);
            map.put("user",user);
            return map;
        }



    }

    /**
     *
     * @param model 携带数据返回
     * @param pageSize 一页显示多少条-非必传值
     * @param pageNum 显示当前页--必传值
     * @return 前端页面
     * @Author: Zoutao
     * @Date: 2018/12/6
     */
//分页查询数据
    @GetMapping("/getLaboratoryInfoList")
    public String LaboratoryInfoList(Model model, LaboratoryInfo laboratoryInfo,
                             String reserve,
                             @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                             @RequestParam(defaultValue="8",value="pageSize")Integer pageSize){

        System.out.println(laboratoryInfo.toString());
        if (!"null".equals(laboratoryInfo.getBuildingName())) {
            model.addAttribute("buildingName",laboratoryInfo.getBuildingName());
        }
        if(!"null".equals(reserve)){
            model.addAttribute("reserve",reserve);
        }
        if (!"null".equals(laboratoryInfo.getCategory())){
            model.addAttribute("category",laboratoryInfo.getCategory());
        }
        if (laboratoryInfo.getStatus() !=null && !(laboratoryInfo.getStatus().equals("null")) && !(laboratoryInfo.getStatus().equals(""))){
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
//            System.out.println(teacherService.getLaboratoryInfoList(laboratoryInfo).toArray().toString());
            List<LaboratoryInfo> userList = teacherService.getLaboratoryInfoList(laboratoryInfo,reserve);
            System.out.println("分页数据："+userList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(userList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/teacher/laboratory_info";
    }


    //预约实验信息表
    @RequestMapping("/reservePage")
    public String reservePage(Model model,@RequestParam(value = "labid") String labid
            ,@RequestParam(value = "reserveTime") String reserveTime){
        model.addAttribute("labid",labid);
        if(!"null".equals(reserveTime)){
            model.addAttribute("reserve",reserveTime);
        }
        Map laboratoryInfo = teacherService.getLaboratoryInfo(labid);
        System.out.println(laboratoryInfo.get("laboratoryInfo"));

        System.out.println("ddddddddddd"+labid+reserveTime);
        System.out.println(laboratoryInfo.get("reserveTimeList"));
        model.addAttribute("laboratoryInfo",laboratoryInfo);
        return "/teacher/reserve_page";
    }

    /**
     * 用户写入预约实验室记录
     */
    @PostMapping(value = "/addReserve")
    @ResponseBody
    public  Reserve addReserve(HttpServletRequest request,LaboratoryInfo laboratoryInfo, String reserveTime, Reserve reserve){
        System.out.println("=============="+reserve.toString());
//        System.out.println(laboratoryInfo.toString()+"========"+reserveTime);
        reserve.setLabid(laboratoryInfo.getLabid());
        String uuid = teacherService.addReserve(reserve);
//
//        if (uuid.equals("false")) {
//            return "forward:/teacher/reservePage";
//        } else{
            return reserve;
//        }
    }

    /**
     * 查看预约详情
     * @return
     */
    @RequestMapping("/detailReservePage")
    public String detailReservePage(String id,Model model){
        System.out.println(id);
        Reserve detailReserve = teacherService.getDetailReserve(id);
        if (detailReserve==null){
            return "redirect:/userLogin";
        }
        model.addAttribute("detailReserve",detailReserve);
        return "/teacher/detail_reserve_page";
    }

    //分页查询预约数据
//    @ResponseBody
    @GetMapping("/getReserveInfoList")
    public String getReserveInfoList(Model model, LaboratoryInfo laboratoryInfo, Reserve reserve,
                             @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                             @RequestParam(defaultValue="8",value="pageSize")Integer pageSize){

        System.out.println(laboratoryInfo.toString());
        if (!"null".equals(laboratoryInfo.getBuildingName())) {
            model.addAttribute("buildingName",laboratoryInfo.getBuildingName());
        }
        if(!"null".equals(reserve.getReserveTime())){
            model.addAttribute("reserveTime",reserve.getReserveTime());
        }
        if (!"null".equals(laboratoryInfo.getCategory())){
            model.addAttribute("category",laboratoryInfo.getCategory());
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
            List<Reserve> userList = teacherService.getReserveList(reserve,laboratoryInfo);
            System.out.println("分页数据："+userList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(userList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/teacher/reserve_info";
    }


    /***
     * 获取个人信息
     */
    @GetMapping("/getTeacherInfo")
    public String getTeacherInfo(Model model){
        TeacherUser teacherInfo = teacherService.getTeacherInfo();
        model.addAttribute("teacherInfo",teacherInfo);
        return "/teacher/teacher_info";
    }


    @PostMapping("/delReserve")
    @ResponseBody
    public Object  delReserve(String id){
        Map map=new HashMap();
        System.out.println(id+"=============");
        boolean bool = teacherService.delReserve(id);
        if (bool){
            map.put("status",200);
            map.put("id",id);
            return map;
        }else {
            map.put("status",201);
            map.put("id",id);
            return map;
        }
//        return "redirect:/teacher/getReserveInfoList";
//
    }


}
