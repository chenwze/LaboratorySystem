//package com.gdufe.laboratorysystem.utils;
//
//import com.gdufe.laboratorysystem.entity.User;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//
///**
// * @program: LaboratorySystem
// * @description: 登录信息
// * @author: chen weize
// * @create: 2022-03-06 18:24
// * @version: 1.0
// */
//public class UserInfo {
//    public static boolean islogin(){
//        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//    public static User getUser(){
//        User user = (User) SecurityContextHolder.getContext()
//                .getAuthentication().getPrincipal();
//        return user;
//    }
//
//    public static Object[] getAuthorities(){
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return userDetails.getAuthorities().toArray();
//    }
//
//    public static  String getUserId(){
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return userDetails.getUsername();
//    }
//
//}
