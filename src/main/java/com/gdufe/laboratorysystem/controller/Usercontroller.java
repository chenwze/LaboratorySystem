package com.gdufe.laboratorysystem.controller;

import com.gdufe.laboratorysystem.utils.UserInfo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cwz
 * @date 年05月24日 19:14
 */
@Controller
public class Usercontroller {

    //跳转登录界面
    @GetMapping(value = {"/login","/userLogin"})
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
                    return "/admin/admin_index";
                }
        }

        return "redirect:/userLogin";
    }



//    //注册页面跳转
//    @GetMapping("/reisterPage")
//    public String toRegister(){
//        return "register/register";
//    }
//    //注册账号register
//    @PostMapping("/register")
//    public String addUser(User user){
//        boolean bool = userService.addUser(user);
//        if (bool){
//            return "login/login";
//        }else {
//            return "redirect:/reisterPage?error";
//        }
//
//    }
//
//    // 向用户登录页面跳转
//    @GetMapping("/userLogin")
//    public String toLoginPage() {
//        return "login/login";
//    }
//
////    //跳转主页
////    @PostMapping("/userLogin")
////    public String toIndex() {
////        System.out.println("sssssssss");
////        return "redirect:/index";
////    }
//
//    // 向用户登录页面跳转
//    @GetMapping("/login")
//    public String login() {
//        return "login/login";
//    }
//
//    //验证登录
//    @GetMapping({"/index", "/",})
//    public String userLogin(Model model, HttpSession session,
//                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                            @RequestParam(value = "pageSize", defaultValue = "8") int pageSize){
//        System.out.println("---------------------------------------");
//        Object[] authorities=new Object[]{};
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (!(auth instanceof AnonymousAuthenticationToken)) {
//            authorities = auth.getAuthorities().toArray();
//        }
//        System.out.println(authorities.toString()+"------------"+authorities.length);
//
//        for (int i=0;i<authorities.length;i++){
//            String state=authorities[i].toString();
//            if(session.getAttribute(state)==null){
//                Page<Moviesohu> moviesohuList= moviesohuService.findAll(state,pageNum,pageSize);
//                session.setAttribute(state,moviesohuList);
//            }
//
//        }
//        return "index";
//    }
//
//    @GetMapping("/getuserByContext")
//    @ResponseBody
//    public Object[] getUser() {
//        // 获取应用上下文
//        SecurityContext context = SecurityContextHolder.getContext();
//        System.out.println("userDetails: "+context);
//        // 获取用户相关信息
//        Authentication authentication = context.getAuthentication();
//        UserDetails principal = (UserDetails)authentication.getPrincipal();
//        System.out.println(principal.toString());
//        Object principal1 = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//        Object[] arr = principal.getAuthorities().toArray();
//        System.out.println("======================"+principal1+"===============");
//        System.out.println("username: "+principal.getUsername()+"---------------------------");
//        System.out.println("passwrod:"+principal.getPassword());
//        return arr;
//    }
//
//    @RequestMapping("/getUsername")
//    public String getUsername(){
//        //获取到登录的用户名 这里的User对象是Spring-Security提供的User
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(user.getPassword());
//        if(user!=null){
//            return null;
//        }else {
//            return null;
//        }
//    }
//    //登录用户信息显示
//    @GetMapping("/getUserInfo")
//    public String getUserInfo (Model model){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            UserDetails principal = (UserDetails)authentication.getPrincipal();
//            model.addAttribute("username",principal.getUsername());
//            model.addAttribute("password",principal.getPassword());
//            model.addAttribute("authorities",principal.getAuthorities());
//            model.addAttribute("isEnabled",principal.isEnabled());
//            model.addAttribute("isAccountNonExpired",principal.isAccountNonExpired());
//            model.addAttribute("isAccountNonLocked",principal.isAccountNonLocked());
//            model.addAttribute("isCredentialsNonExpired",principal.isCredentialsNonExpired());
//        }
//        return "user/user_info";
//    }

}
