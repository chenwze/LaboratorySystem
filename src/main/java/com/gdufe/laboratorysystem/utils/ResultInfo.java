//package com.gdufe.laboratorysystem.utils;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @program: LaboratorySystem
// * @description: 返回结果集
// * @author: chen weize
// * @create: 2022-03-31 15:49
// * @version: 1.0
// */
//public class ResultInfo implements Serializable {
//    private static final long serialVersionUID = 8247836128630200489L;
//    private String code;
//    private String msg;
//    private Map<String, Object> data;
//
//    @org.jetbrains.annotations.NotNull
//    public static ResultInfo success(HashMap<String,Object> data) {
//        ResultInfo resultInfo = new ResultInfo();
//        resultInfo.setData(data);
//        resultInfo.setCode("200");
//        resultInfo.setMsg("请求成功!!!");
//        return resultInfo;
//    }
//    public static Result succ(String mess, Object data) {
//        Result m = new Result();
//        m.setCode("0");
//        m.setData(data);
//        m.setMsg(mess);
//        return m;
//    }
//    public static Result fail(String mess) {
//        Result m = new Result();
//        m.setCode("-1");
//        m.setData(null);
//        m.setMsg(mess);
//        return m;
//    }
//    public static Result fail(String mess, Object data) {
//        Result m = new Result();
//        m.setCode("-1");
//        m.setData(data);
//        m.setMsg(mess);
//        return m;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public Map<String, Object> getData() {
//        return data;
//    }
//
//    public void setData(Map<String, Object> data) {
//        this.data = data;
//    }
//}
