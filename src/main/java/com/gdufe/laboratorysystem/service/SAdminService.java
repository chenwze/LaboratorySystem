package com.gdufe.laboratorysystem.service;


import com.gdufe.laboratorysystem.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-25 00:02
 * @version: 1.0
 */
public interface SAdminService {
    //管理员列表
    List<User> getAdminUserList(User user);

    //重置密码
    HashMap resetPasssword(String username);
    //添加管理员账号
    HashMap addAdminUser(User user);
    //管理员信息
    User getAdminUser(String usename);

    //更新管理员账号信息
    HashMap upAdminUserIfo(MultipartFile file, User name);

    //批量删除管理员账号
    HashMap delAdminUserList(String[] ids);
}
