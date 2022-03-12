package com.gdufe.laboratorysystem.controller;

import com.gdufe.laboratorysystem.entity.LaboratoryInfo;
import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.Reserve;
import com.gdufe.laboratorysystem.entity.User;

import com.gdufe.laboratorysystem.service.StudentService;
import com.gdufe.laboratorysystem.utils.UserInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: LaboratorySystem
 * @description: 学生权限操作
 * @author: chen weize
 * @create: 2022-03-06 12:26
 * @version: 1.0
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @RequestMapping("/student_index")
    public String studentIndex(){
    return "/student/student_main";
    }

    //用户个人账号信息
    @RequestMapping("/user")
    public String userPage(Model model){
        System.out.println(UserInfo.islogin()+"+++++++++");
        if (UserInfo.islogin()){
            System.out.println(UserInfo.getUser().getUsername());
            User user = studentService.getStudentUser(UserInfo.getUser().getUsername());
            System.out.println(user.toString());
            model.addAttribute("StudentUser",user);
            return"/student/student_user";
        }else {
            return "redirect:/userLogin";
        }

    }


    //查看公告
    @RequestMapping("/showNotice")
    public String noticePage(Model model){
        List<Notice> showNotice = studentService.getShowNotice();
        for (Notice noticeList:showNotice){
            System.out.println(noticeList.toString());
        }
        model.addAttribute("notices",showNotice);
        return "/student/studnet_notice";
    }

    @RequestMapping("/changePassword")
    public String changePasswordPage(Model model){
        System.out.println("+++++++++++");
        if (UserInfo.islogin()){
            System.out.println(UserInfo.getUser().getUsername());
            model.addAttribute("username",UserInfo.getUser().getUsername());
            return "/student/change_password";
        }else {
            return "redirect:/userLogin";
        }

    }

//    //获取实验室信息列表
//    @RequestMapping("/getLaboratoryInfoList")
//    public String getLaboratoryInfoList(Model model, LaboratoryInfo laboratoryInfo){
//        PageInfo<LaboratoryInfo> laboratoryInfoList = studentService.getLaboratoryInfoList(laboratoryInfo);
//        model.addAttribute("data",laboratoryInfoList);
//        return "/student/laboratory_info";
//    }
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
            List<LaboratoryInfo> userList = studentService.getLaboratoryInfoList(laboratoryInfo,reserve);
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
        return "/student/laboratory_info";
    }


    //预约实验信息表
    @RequestMapping("/reservePage")
    public String reservePage(Model model,@RequestParam(value = "labid") String labid
            ,@RequestParam(value = "reserve") String reserve){
        model.addAttribute("labid",labid);
        if(!"null".equals(reserve)){
            model.addAttribute("reserve",reserve);
        }
        Map laboratoryInfo = studentService.getLaboratoryInfo(labid);
        System.out.println(laboratoryInfo.get("laboratoryInfo"));

        System.out.println("ddddddddddd"+labid+reserve);
        System.out.println(laboratoryInfo.get("reserveTimeList"));
        model.addAttribute("laboratoryInfo",laboratoryInfo);
        return "/student/reserve_page";
    }

    /**
     * 用户写入预约实验室记录
     */
    @PostMapping(value = "/addReserve")
    @ResponseBody
    public String addReserve(LaboratoryInfo laboratoryInfo, String reserveTime, Reserve reserve){
        System.out.println("==============");
        System.out.println(laboratoryInfo.toString()+"========"+reserveTime);
        reserve.setLabid(laboratoryInfo.getLabid());
        boolean bool = studentService.addReserve(reserve);

        if (bool) {
            return "alert('sss');";
        } else{
            return "false";
        }
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
            List<Reserve> userList = studentService.getReserveList(reserve,laboratoryInfo);
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
        return "/student/reserve_info";
    }

}
