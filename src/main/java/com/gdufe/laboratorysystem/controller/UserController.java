package com.gdufe.laboratorysystem.controller;

import com.gdufe.laboratorysystem.entity.VerificationCode;
import com.gdufe.laboratorysystem.service.impl.UserServiceImpl;
//import com.gdufe.laboratorysystem.utils.UserInfo;
import com.gdufe.laboratorysystem.utils.VerCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.RequestMapping;
import com.gdufe.laboratorysystem.entity.User;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author cwz
 * @date 年05月24日 19:14
 */
@Controller
public class UserController {
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private UserServiceImpl userServiceImpl;


    /**
     * 发送验证码到指定邮箱
     */
    @GetMapping("/user/verCode")
    @ResponseBody
    public Map verCode(HttpServletRequest request,String username, String userType, String email) throws MessagingException {
        HashMap map = new HashMap();
        System.out.println("email = " + email);
        boolean bolCode = request.getSession().getAttribute("code") == null;
        if (bolCode){
            String code= VerCodeGenerateUtil.generateVerCode();
            System.out.println("checkCode = " + code);
//            final HttpSession httpSession=request.getSession();
            request.getSession().setAttribute("code",code);
            request.getSession().setAttribute("email",email);
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setCode((String) request.getSession().getAttribute("code"));
            verificationCode.setEmail(email);
            HttpSession session = request.getSession();
            userServiceImpl.sendEmail(verificationCode);
            try {
                //TimerTask实现5分钟后从session中删除code
                final Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        session.removeAttribute("code");
                        System.out.println("checkCode删除成功");
                        timer.cancel();
                    }
                },5*60*1000);
                map.put("status","200");
                map.put("msg","发送成功！！");
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                map.put("status","500");
                map.put("msg","发生错误！！");
                return map;
            }
        }else {
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setCode((String) request.getSession().getAttribute("code"));
            verificationCode.setEmail(email);
            userServiceImpl.sendEmail(verificationCode);
            map.put("status","200");
            map.put("msg","发送成功！！");
            return map;
        }

    }


    /**
     * 忘记密码修改请求
     * @return
     */
    @RequestMapping("/user/retrievePassword")
    @ResponseBody
    public Map retrievePassword(HttpServletRequest request ,String email
            ,String password,String code,String username,String usertype){
        System.out.println("email = " + email + ", password = " + password + ", code = " + code);
        System.out.println(request.getSession().getAttribute("code"));
        HashMap map = new HashMap();
        map.put("status","201");

        if (email == null && email ==""){
            map.put("msg","邮件内容为空！！");
            return map;
        }

        if (username == null && username ==""){
            map.put("msg","用户账号为空！！");
            return map;
        }
        if (password == null && password ==""){
            map.put("msg","修改密码为空！！");
            return map;
        }
        if (code == null && code ==""){
            map.put("msg","验证码为空！！");
            return map;
        }

        if (usertype == null && usertype ==""){
            map.put("msg","用户不能为空！！");
            return map;
        }
        if (!request.getSession().getAttribute("code").equals(code)){
            System.out.println(request.getSession().getAttribute("code")+code);
            map.put("msg","验证码错误！！");
            return map;
        }
        if(!request.getSession().getAttribute("email").equals(email)){
            map.put("msg","邮箱不正确！！");
            return map;
        }
        request.getSession().removeAttribute("code");
        request.getSession().removeAttribute("email");
        map = userServiceImpl.retrievePassword(usertype,username, password, code, email);
        return map;
    }

    //跳转登录界面
    @GetMapping(value = {"/login","/userLogin","/userlogin"})
    public String loginPage(){
        System.out.println("进入登录页面---------");
        return "login";
    }

    //跳转用户的主页面
    @GetMapping(value = {"/"})
    public String indexPage(){
        //判断是否登录
        if( ! SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return "redirect:/userLogin";
        }

        //获取用户的所有权限
        Object[] toArray = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
        for (Object array:toArray) {
                //含有student权限跳转学生页面
                if (array.toString().equals("student")){
                    System.out.println("++++++++++++++++"+array);
                    return "/student/student_main";
                }
                 //含有teachert权限跳转老师页面
                if (array.toString().equals("teacher")){
                    System.out.println("++++++++++++++++"+array);
                    return "/teacher/teacher_main";
                }
                //含有admin权限跳转管理员页面
                if(array.toString().equals("admin")){
                    System.out.println("++++++++++++++++"+array);
                    return "/admin/admin_main";
                }
        }

        return "redirect:/userLogin";
    }

    /**
     * 忘记密码，打开找回密码页面
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/user/retrievePasswordPage")
    public String retrievePasswordPage(Model model,User user,String username){
        System.out.println("user = " + user.toString()+username);
        if(user.getUsername() != null && !user.getUsername().equals("") ){
            model.addAttribute("username",user.getUsername());
        }
        return "/retrieve_password";
    }


    @RequestMapping(value = "/error400Page")
    public String error400Page() {
        return "/error/400";
    }

    @RequestMapping(value = "/error403Page")
    public String error401Page() {
        return "/error/403";
    }

    @RequestMapping(value = "/error404Page")
    public String error404Page(Model model,HttpServletRequest request) {
        model.addAttribute("code","6666666");
        model.addAttribute("msg",request.getRequestURL().toString());
        return "/error/404";
    }
    @RequestMapping(value = "/error500Page")
    public String error500Page(Model model) {
        return "/error/500";
    }
}
