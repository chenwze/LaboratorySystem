package com.gdufe.laboratorysystem.utils;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-03-27 20:25
 * @version: 1.0
 */


import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;

public class ExeImport {

    public static void main(String[] args) throws Exception {
//        //Excel办公，以表格形式存在
//        //如何实现：1.fastExcel 2.jxl解析（第三方） 3.poi方式（第三方）
//        //通过jxl方式解析Excel步骤如下：
//        //1. 导入jxl的jar包
//        //2. 获取到Excel文件
//        File file = new File("C:\\Users\\飞翔的兰\\Desktop\\student.xls");
//        Workbook wb =  Workbook.getWorkbook(file);
//        //3. 获取指定的sheet页码   通过指定的Sheet页的名字获取指定的Sheet页，也可以通过索引获取Sheet
//        Sheet sheet = wb.getSheet("Sheet1");
//        //4. 获取指定的单元格的数据  通过getCell方法获取指定单元格对象，参数是column,row,索引从0开始
////		Cell cell = sheet.getCell(0,0);
////		System.out.println(cell.getContents());
//
//        //4.1 循环获取指定的行和列的单元格的值     外循环控制行，内循环控制列
//        for (int i = 0; i < sheet.getRows(); i++) {
//            for (int j = 0; j < sheet.getColumns(); j++) {
//                Cell cell = sheet.getCell(j,i);
//                System.out.print(cell.getContents()+"\t");      //\t代表tab键的字符
//            }
//            System.out.println();               //设置每查询完一行就换行
//        }
//        wb.close();    //将工作簿的资源关闭
//    }

    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(File file) {
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader((File) file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
