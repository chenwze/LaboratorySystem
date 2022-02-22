package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.StudentDao;
import com.gdufe.laboratorysystem.entity.Student;
//import com.gdufe.laboratorysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    StudentDao studentDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userType=request.getParameter("usertype");
        if (userType.equals("1")){
            System.out.println("学生用户进行登录验证-----------");
//            Student student=studentService.getStudent(username);
//            Student student=new Student();
//            System.out.println(request.getParameter("username")+request.getParameter("password"));
//            student.setUsername("123");
//            System.out.println(request.getParameter("username").equals(student.getUsername()));
//            student.setPassword("123");
//            System.out.println(student.toString()+"---------------");
//            Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
//            student.setPassword(encoder.encode(student.getPassword()));
//            System.out.println(encoder.encode("111111"));

//            List<Role> authorities = userMapper.getUserRolesByUid(user.getId());
//            List<GrantedAuthority> authorityList = new ArrayList<>();
//            for (Role authority : authorities) {
//                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(student.getUsername());
//                authorityList.add(grantedAuthority);
//            }
            Student student = studentDao.getStudent(username);
            List<GrantedAuthority> authorityList = new ArrayList<>();
            System.out.println(student.toString());
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(student.getRole());
            authorityList.add(new SimpleGrantedAuthority(student.getRole()));
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(student.getUserid(),
                student.getPassword(), authorityList);
            System.out.println("学生用户进行登录验证-----------");

            return userDetails;
        }

        if (userType.equals("1")){

        }

        if (userType.equals("tec")){

        }
//        //创建UserDetails对象，设置用户名、密码和权限
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
//                user.getPassword(), authorityList);
//        throw  new UsernameNotFoundException("当前用户不存在！");

        return  null;
    }
}