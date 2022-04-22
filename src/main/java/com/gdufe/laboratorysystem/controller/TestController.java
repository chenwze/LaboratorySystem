//package com.gdufe.laboratorysystem.controller;
//
///**
// * @program: LaboratorySystem
// * @description:
// * @author: chen weize
// * @create: 2022-03-13 01:51
// * @version: 1.0
// */
//
//import com.gdufe.laboratorysystem.utils.CommonExcel;
//import com.gdufe.laboratorysystem.utils.ImportExcel;
//import org.apache.poi.hssf.usermodel.*;
//import org.apache.poi.hssf.util.HSSFColor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFRichTextString;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.HSSFColor;
///**
// * @Author https://www.javastudy.cloud
// * @CreateTime 2019/11/8
// **/
//@Controller
//public class TestController {
////    @PostMapping("/test/testAjax1")
////    public void testAjax1() throws Exception {
////        HttpServletResponse response=null;
////        String title = "xx报表";
////        String[] rowsName = new String[]{"oo","xx","oo","xx","xx","oo"};
////        List<Object[]> dataList = new ArrayList<Object[]>();
////        Object[] objs = null;
////        for (int i = 0; i < 10; i++) {
////            objs = new Object[rowsName.length];
////            objs[0] = i;
////            objs[1] = i;
////            objs[2] = i;
////            objs[3] = i;
////            objs[4] = i;
////            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            String date = df.format(new Date());
////            objs[5] = date;
////            dataList.add(objs);
////        }
////        String fileName="xx报表-"+String.valueOf(System.currentTimeMillis()).substring(4,13)+".xls";
////        CommonExcel ex = new CommonExcel(title, rowsName, dataList,response,fileName);
////        ex.downloadExcel();
////    }
//    /**
//     * 导出数据为Excel文件
//     * 下面注释的代码为直接响应到服务器的
//     * @param request
//     * @param response
//     * @return
//     */
//    @GetMapping("/test/testAjax1")
//    public void exportExce1l(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
//////        List<TzJobSet> list = tzJobSetService.list();
////        String resultName ="";
////        String[] title = {"ID", "标题", "排序", "创建时间"};
//////        String filename = "jobSet.xls";
////        String sheetName = "职称设置表";
//////        String[][] content = new String[list.size()][4];
////        try {
//////            for (int i = 0; i < list.size(); i++) {
//////                content[i][0] = String.valueOf(list.get(i).getId());
//////                content[i][1] = list.get(i).getTitle();
//////                content[i][2] = String.valueOf(list.get(i).getSortId());
//////                content[i][3] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getAddTime());
//////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        HSSFWorkbook wb = ImportExcel.getHSSFWorkbook(sheetName, title, null);
////        try {
////            String ctxPath = "D://upFiles";
////            String name = new SimpleDateFormat("ddHHmmss").format(new Date());
////            String fileName=name+"jobSet.xlsx";
////            String bizPath = "files";
////            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
////            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
////            if (!file.exists()) {
////                file.mkdirs();// 创建文件根目录
////            }
////            String savePath = file.getPath() + File.separator + fileName;
////            resultName = bizPath + File.separator + nowday+ File.separator + fileName;
////            if (resultName.contains("\\")) {
////                resultName = resultName.replace("\\", "/");
////            }
////            System.out.print(resultName);
////            System.out.print(savePath);
////            // 响应到客户端需要下面注释的代码
//////            this.setResponseHeader(response, filename);
//////            OutputStream os = response.getOutputStream(); //响应到服务器
////            OutputStream os = new FileOutputStream(savePath); // 保存到当前路径savePath
////            wb.write(os);
////            os.flush();
////            os.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return resultName;
//
//
//        response.setContentType("octets/stream");
////      response.addHeader("Content-Disposition", "attachment;filename=test.xls");
//        String excelName = "学生信息表";
//        //转码防止乱码
//        response.addHeader("Content-Disposition", "attachment;filename="+new String( excelName.getBytes("gb2312"), "ISO8859-1" )+".xls");
//        String[] headers = new String[]{"编号","姓名","年龄","性别"};
//        try {
//            OutputStream out = response.getOutputStream();
//            exportExcel(excelName,headers, getList(), out,"yyyy-MM-dd");
//            out.close();
//            System.out.println("excel导出成功！");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     *
//     * @Description: 模拟从数据库中查询出来的数据，一般是数据表中的几列
//     * @Auther: lujinyong
//     * @Date: 2013-8-22 下午2:53:58
//     */
//    public List<Map<String,Object>> getList(){
//        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//        for(int i = 1; i<100;i++){
//            Map<String,Object> map = new HashMap<String, Object>();
//            map.put("number",1000+i);
//            map.put("name", "张三"+i);
//            int age = (int)(Math.random()*100);
//            do{
//                age = (int)(Math.random()*100);
//            }while(age<10||age>15);
//            map.put("age", age);
//            map.put("sex", age%2==0?0:1);//获得随机性别
//            list.add(map);
//        }
//        return list;
//    }
//    /**
//     *
//     * @Description: 生成excel并导出到客户端（本地）
//     * @Auther: lujinyong
//     * @Date: 2013-8-22 下午3:05:49
//     */
//    protected void exportExcel(String title,String[] headers,List mapList,OutputStream out,String pattern){
//        //声明一个工作簿
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        //生成一个表格
//        HSSFSheet sheet = workbook.createSheet(title);
//        //设置表格默认列宽度为15个字符
//        sheet.setDefaultColumnWidth(20);
//        //生成一个样式，用来设置标题样式
//        HSSFCellStyle style = workbook.createCellStyle();
//        //设置这些样式
////        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
////        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
////        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
////        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
////        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
////        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
////        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        //生成一个字体
////        HSSFFont font = workbook.createFont();
////        font.setColor(HSSFColor.VIOLET.index);
////        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
////        //把字体应用到当前的样式
////        style.setFont(font);
////        // 生成并设置另一个样式,用于设置内容样式
////        HSSFCellStyle style2 = workbook.createCellStyle();
////        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
////        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
////        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
////        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
////        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
////        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
////        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
////        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
////        // 生成另一个字体
////        HSSFFont font2 = workbook.createFont();
////        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
////        // 把字体应用到当前的样式
////        style2.setFont(font2);
////        //产生表格标题行
//
//        HSSFCellStyle style1 = getStyle(workbook);
////        style1.setFont(fonn);
//        HSSFRow row = sheet.createRow(0);
//        for(int i = 0; i<headers.length;i++){
//            HSSFCell cell = row.createCell(i);
//            cell.setCellStyle(style);
//            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
//            cell.setCellValue(text);
//        }
//        for (int i=1;i<mapList.size();i++) {
//            Map<String,Object> map = (Map<String, Object>) mapList.get(i);
//            row = sheet.createRow(i+1);
//            int j = 0;
//            Object value = null;
//            value=map.get("number");
//            if(value instanceof Integer){
//                row.createCell(j++).setCellValue(String.valueOf(value));
//            }
//            row.createCell(j++).setCellValue(map.get("name").toString());
//            value=map.get("age");
//            if(value instanceof Integer){
//                row.createCell(j++).setCellValue(String.valueOf(value));
//            }
//            row.createCell(j++).setCellValue("0".equals(map.get("sex").toString())?"女":"男");
//        }
//        try {
//            workbook.write(out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    /*
//     * 列数据信息单元格样式
//     */
//    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
//        // 设置字体
//        HSSFFont font = workbook.createFont();
//        //设置字体名字
//        font.setFontName("微软雅黑");
//        //设置样式;
//        HSSFCellStyle style = workbook.createCellStyle();
//        //设置底边框;
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        //设置底边框颜色;
//        style.setBottomBorderColor(HSSFColor.BLACK.index);
//        //设置左边框;
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        //设置左边框颜色;
//        style.setLeftBorderColor(HSSFColor.BLACK.index);
//        //设置右边框;
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        //设置右边框颜色;
//        style.setRightBorderColor(HSSFColor.BLACK.index);
//        //设置顶边框;
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        //设置顶边框颜色;
//        style.setTopBorderColor(HSSFColor.BLACK.index);
//        //在样式用应用设置的字体;
//        style.setFont(font);
//        //设置自动换行;
//        style.setWrapText(false);
//        //设置水平对齐的样式为居中对齐;
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//        //设置垂直对齐的样式为居中对齐;
//        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//        return style;
//    }
//}
