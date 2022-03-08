package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.AdminUserDao;
import com.gdufe.laboratorysystem.dao.StudentUserDao;
import com.gdufe.laboratorysystem.dao.TeacherUserDao;
//import com.gdufe.laboratorysystem.service.UserService;
import com.gdufe.laboratorysystem.entity.User;
import com.gdufe.laboratorysystem.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-02-05 16:53
 * @version: 1.0
 */
@Service
public  class UserServiceImpl implements UserDetailsService {

    @Value("${system.user.password.secret}")
    private String secret;

    @Autowired
    StudentUserDao studentUserDao;

    @Autowired
    TeacherUserDao teacherUserDao;

    @Autowired
    AdminUserDao adminUserDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userType=request.getParameter("usertype");
        String password=request.getParameter("password");

//        if (!isInteger(username) || userType==null ){
//            throw new BadCredentialsException("用户数据类型错误");
//        }

        //学生认证
        if (userType.equals("2")){
            System.out.println("学生用户进行登录验证---------------------");
            //根据用户名查找账号信息
            User user = studentUserDao.getStudent(username);
            System.out.println("学生用户进行登录验证-----------");
            return loginDo(user,username,password);
        }

        //教师认证
        if (userType.equals("1")){
            User user = teacherUserDao.getTeacher(username);
            return loginDo(user,username,password);
        }

        //管理员认证
        if (userType.equals("0")){
            User user = adminUserDao.getAdmin(username);
            return loginDo(user,username,password);
        }

        throw new BadCredentialsException("用户类型不存在");
    }

    //用户验证账号是否正常
    public UserDetails loginDo(User user,String username,String password){
        if (user==null || user.equals(null)){
            throw new BadCredentialsException("用户" + username + " 不存在");
        }

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        if(!encoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("用户密码错误。");
        }
        if (!user.getStatus().equals("正常")){
            throw new BadCredentialsException("用户:"+user.getStatus());
        }
        return user;
    }


    /**
     * 判断是否是int数值
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}