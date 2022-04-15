package com.gdufe.laboratorysystem.utils;

import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @program: LaboratorySystem
 * @description:
 * @author: chen weize
 * @create: 2022-04-03 20:37
 * @version: 1.0
 */
public class ImgHeadUtils {

    /**
     * 保存图片，返回路径
     * @param file
     * @return
     */
    public  static  String imgHead(MultipartFile file){
        String url_path =null;
        if (file != null && !file.isEmpty()){
            System.out.println("AdminController.upUserInfo");
            //获取项目classes/static的地址
            String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
            System.out.println("staticPath = " + staticPath);
            String fileName = file.getOriginalFilename();  //获取文件名
            System.out.println("fileName = " + fileName);
            fileName= UUID.randomUUID().toString().replaceAll("-","")+fileName;
            System.out.println("fileName = " + fileName);
            // 图片存储目录及图片名称
            url_path = "/login/imghead" + File.separator + fileName;
            //图片保存路径
            String savePath = staticPath + File.separator + url_path;
            System.out.println("图片保存地址："+savePath);
            // 访问路径=静态资源路径+文件目录路径
            String visitPath ="static/" + url_path;
            System.out.println("图片访问uri："+visitPath);

            File saveFile = new File(savePath);
            if (!saveFile.getParentFile().exists()){
                saveFile.getParentFile().mkdirs();
            }
            try {
                System.out.println("saveFile = " + saveFile);
                file.transferTo(saveFile);  //将临时存储的文件移动到真实存储路径下
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return url_path.toString().replaceAll("\\\\","/");
    }
}
