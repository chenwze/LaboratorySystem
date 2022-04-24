package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.*;
import com.gdufe.laboratorysystem.entity.*;
import com.gdufe.laboratorysystem.service.AdminService;
import com.gdufe.laboratorysystem.service.SAdminService;
import com.gdufe.laboratorysystem.utils.ImgHeadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-25 00:02
 * @version: 1.0
 */
@Service
public class SAdminServiceImpl implements SAdminService {

    @Autowired
    NoticeDao noticeDao;

    @Autowired
    StudentUserDao studentUserDao;

    @Autowired
    TeacherUserDao teacherUserDao;

    @Autowired
    LaboratoryInfoDao laboratoryInfoDao;
    @Autowired
    ReserveDao reserveDao;
    @Autowired
    AdminUserDao adminUserDao;
    @Autowired
    LaboratoryThingDao laboratoryThingDao;

    @Value("${system.user.password.secret}")
    private String secret;
    /*更新认证信息*/
    public static void setLoginUser(UserDetails userDetails) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }


    //管理员账号列表
    @Override
    public  List<User> getAdminUserList(User user) {
        List<User> adminUserList = adminUserDao.getAdminUserList(user);
        return adminUserList;
    }

    //重置密码
    @Override
    public HashMap resetPasssword(String username){
        HashMap hashMap = new HashMap();
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        String password = encoder.encode(username);
        int i = adminUserDao.resetPasssword(username, password);
        if (i == 1){
            hashMap.put("status","200");
            hashMap.put("msg","重置密码成功！你的密码是"+username);
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","重置密码失败！！");
            return hashMap;
        }

    }

    //添加管理员账号
    @Override
    public HashMap addAdminUser(User user){
        HashMap hashMap = new HashMap();

        if (adminUserDao.existEmail(user.getEmail(),"")){
            hashMap.put("status","201");
            hashMap.put("msg","添加失败,邮箱已存在！！");
            return hashMap;
        }

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        String password = encoder.encode(user.getPassword());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setPassword(password);
        user.setUserid(UUID.randomUUID().toString().replaceAll("-",""));
        user.setCreateTime(df.format(new Date()));
        int i = adminUserDao.addAdminUser(user);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","添加成功！！");
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","添加失败！！");
            return hashMap;
        }
    }


    //管理员信息
    @Override
    public User getAdminUser(String usename){
        User admin = adminUserDao.getAdmin(usename);
        return admin;
    }


    //更新管理员账号信息
    @Override
    @Transactional
    public HashMap upAdminUserIfo(MultipartFile file, AdminUser adminUser){
        HashMap hashMap = new HashMap();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //获取当前用户名
            String currentUserName = authentication.getName();
            adminUser.setUsername(currentUserName);
//            hashMap.put("msg","未登录");
//            hashMap.put("status","201");
        }

        if(adminUserDao.existEmail(adminUser.getEmail(),adminUser.getUsername())){
            hashMap.put("status","201");
            hashMap.put("msg","更新失败，邮箱已存在！！");
            return hashMap;
        }

        String urlPasth=null;
        if (file != null && !file.isEmpty()){
            urlPasth = ImgHeadUtils.imgHead(file);
        }

        if (urlPasth != null){
            adminUser.setHeadPortrait(urlPasth);
        }

        int i = adminUserDao.upSAdminUserInfo(adminUser);
        if (i==1){
            hashMap.put("status","200");
            hashMap.put("msg","更新成功");
            //更新信息

            adminUser = adminUserDao.getAdmin(adminUser.getUsername());
            setLoginUser(adminUser);
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","更新失败，请重试！！");
        }
        return hashMap;
    }

    //批量删除管理员账号
    @Override
    public HashMap delAdminUserList(String[] ids){
        HashMap hashMap = new HashMap();
        int i = adminUserDao.delAdminUserList(ids);
        if (i>0){
            hashMap.put("status","200");
            hashMap.put("msg","删除成功！！");
            hashMap.put("num",i);
            return hashMap;
        }else{
            hashMap.put("status","201");
            hashMap.put("msg","删除失败！！");
//            hashMap.put("num",i);
            return hashMap;
        }

    }
}
