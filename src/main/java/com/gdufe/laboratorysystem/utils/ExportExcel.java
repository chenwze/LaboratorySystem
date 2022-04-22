//package com.gdufe.laboratorysystem.utils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.poi.hssf.usermodel.*;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @program: LaboratorySystem
// * @description:
// * @author: chen weize
// * @create: 2022-03-22 15:37
// * @version: 1.0
// */
//public class ExportExcel {
//    public static void exportExcel(String title, String[] headers, String strs, OutputStream out, String pattern){
//        //声明一个工作簿
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        //生成一个表格
//        HSSFSheet sheet = workbook.createSheet(title);
//        //设置表格默认列宽度为15个字符
//        sheet.setDefaultColumnWidth(20);
//        //生成一个样式，用来设置标题样式
//        HSSFCellStyle style = workbook.createCellStyle();
//        HSSFRow row = sheet.createRow(0);
//        for(int i = 0; i<headers.length;i++){
//            HSSFCell cell = row.createCell(i);
//            cell.setCellStyle(style);
//            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
//            cell.setCellValue(text);
//        }
////        HashMap map = JSON.parseObject(strs, HashMap.class);
//        JSONObject jsonObject = JSONObject.parseObject(strs);
//        JSONArray jsonArray = jsonObject.getJSONArray("laboratoryThingList");
//        System.out.println("+++++"+jsonObject);
//        for (int i=0;i<jsonObject.size();i++) {
////            Map<String,Object> map = (Map<String, Object>) list.get(i);
//
////            row = sheet.createRow(i+1);
////            int j = 0;
////            Object value = null;
////            row.createCell(j++).setCellValue((Boolean) map.get("id"));
////            value=map.get("number");
////            if(value instanceof Integer){
////                row.createCell(j++).setCellValue(String.valueOf(value));
////            }
////            row.createCell(j++).setCellValue(map.get("name").toString());
////            value=map.get("age");
////            if(value instanceof Integer){
////                row.createCell(j++).setCellValue(String.valueOf(value));
////            }
////            row.createCell(j++).setCellValue("0".equals(map.get("sex").toString())?"女":"男");
//        }
//        try {
//            workbook.write(out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
