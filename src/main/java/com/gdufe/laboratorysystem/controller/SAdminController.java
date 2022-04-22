package com.gdufe.laboratorysystem.controller;

import com.gdufe.laboratorysystem.entity.User;
import com.gdufe.laboratorysystem.service.SAdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @version: 1.0
 */
@Controller
@RequestMapping("/sAdmin")
public class SAdminController {
    @Autowired
    SAdminService sAdminService;

    @RequestMapping("/getAdminUserList")
    public String getAdminUserList(User user, Model model,
                                     @RequestParam(required = false,defaultValue="1",value="pageNum")Integer pageNum,
                                     @RequestParam(defaultValue="8",value="pageSize")Integer pageSize) {

        if (!"null".equals(user.getUsername())) {
            model.addAttribute("username",user.getUsername());
        }
        if(!"null".equals(user.getStatus())){
            model.addAttribute("status", user.getStatus());
        }
        if (!"null".equals(user.getEmail())){
            model.addAttribute("email",user.getEmail());
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
            List<User> adminUserList = sAdminService.getAdminUserList(user);
            System.out.println("分页数据："+adminUserList);
            //3.使用PageInfo包装查询后的结果,5是连续显示的条数,结果list类型是Page<E>
            PageInfo pageInfo = new PageInfo(adminUserList);
            //4.使用model/map/modelandview等带回前端
            model.addAttribute("pageInfo",pageInfo);
        }finally {
            PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
        }
        //5.设置返回的jsp/html等前端页面
        // thymeleaf默认就会拼串classpath:/templates/xxxx.html
        return "/admin/admin_user_list";
    }

    //重置管理员账号密码
    @RequestMapping("/resetAdminPasssword")
    @ResponseBody
    public HashMap resetadminPasssword(String username ){
        HashMap hashMap = new HashMap();

        hashMap = sAdminService.resetPasssword(username);

        return hashMap;
    }

    //添加管理员账号页面
    @RequestMapping("/addAdminUserPage")
    public String addAdminPage(){
        return "/admin/admin_user_add";
    }
    //添加管理员账号
    @RequestMapping("/addAdminUser")
    @ResponseBody
    public HashMap addAdminUser(User user){
        HashMap hashMap = new HashMap();
        hashMap  = sAdminService.addAdminUser(user);
        return  hashMap;
    }

    //管理员信息页
    @RequestMapping("/detailAdminUserPage")
    public String detailAdminUserPage(String username,Model model){
        User adminUser = sAdminService.getAdminUser(username);
        model.addAttribute("adminUser",adminUser);
        return "/admin/admin_user_detail";
    }

    //管理员信息编辑页
    @RequestMapping("/editAdminUserPage")
    public String  editadminUser(String username,Model model){
        User adminUser = sAdminService.getAdminUser(username);
        System.out.println(adminUser.toString());
        model.addAttribute("adminUser",adminUser);
        return "/admin/super_admin_user_edit";
    }

    //更新管理账号信息
    @RequestMapping("/upUserInfo")
    @ResponseBody
    public Map upUserInfo(MultipartFile file, User user) {
        System.out.println("user.getName() = " + user.getName());
        HashMap hashMap = sAdminService.upAdminUserIfo(file, user);
        return hashMap;
    }

    //批量删除管理员账号
    @RequestMapping("/delAdminUserList")
    @ResponseBody
    public HashMap delAdminUserList(String[] ids){
        HashMap hashMap = new HashMap();
        hashMap = sAdminService.delAdminUserList(ids);
        return hashMap;
    }

}
