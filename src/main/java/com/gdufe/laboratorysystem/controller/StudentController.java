package com.gdufe.laboratorysystem.controller;

import com.gdufe.laboratorysystem.entity.Notice;
import com.gdufe.laboratorysystem.entity.User;

import com.gdufe.laboratorysystem.service.StudentService;
import com.gdufe.laboratorysystem.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

}
