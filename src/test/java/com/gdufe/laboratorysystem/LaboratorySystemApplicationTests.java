package com.gdufe.laboratorysystem;

import com.gdufe.laboratorysystem.dao.LaboratoryInfoDao;
import com.gdufe.laboratorysystem.dao.StudentInfoDao;
import com.gdufe.laboratorysystem.dao.StudentUserDao;
import com.gdufe.laboratorysystem.entity.LaboratoryInfo;
import com.gdufe.laboratorysystem.entity.StudentInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.UUID;

@SpringBootTest
class LaboratorySystemApplicationTests {


   


    @Autowired
    StudentUserDao studentUserDao;
    @Autowired
    StudentInfoDao studentInfoDao;
    @Autowired
    LaboratoryInfoDao laboratoryInfoDao;


    @Test
    void getTest(){


            int[] a={1,27,5,7,3};
            int n=a.length;
            int i, j;
            for (i = 0; i < n; i++) {//表示 n 次排序过程。
                for (j = 0; j < n - i-1; j++) {
                    if (a[j] < a[j+1]) {//前面的数字大于后面的数字就交换
                        //交换 a[j-1]和 a[j]
                        int temp;
                        temp = a[j];
                        a[j] = a[j+1];
                        a[j+1] = temp;
                    }
                }
            }
        for (int aa :a) {
            System.out.println(aa);

        }

//            for (int i = 0; i < 5; i++) {
//                //注意replaceAll前面的是正则表达式
//                String uuid = UUID.randomUUID().toString().replaceAll("-","");
//                System.out.println(uuid);
////            System.out.println(uuid.length());
//            }

//        Date date= new Date(System.currentTimeMillis());
//        System.out.println(date+"-----"+date.toString());
//        LaboratoryInfo laboratoryInfo=new LaboratoryInfo();
//        laboratoryInfo.setBuildingName("实验楼1");
//        System.out.println(laboratoryInfoDao.getLaboratoryInfoList(laboratoryInfo));
//        System.out.println(studentDao.getUserInfo("10001").toString());
//        System.out.println(studentInfoDao.getStudentInfo("10001").toString());
//        StudentInfo studentInfo=new StudentInfo();
//        studentInfo.setAge(18);
//        studentInfo.setCollege("信息");
//        studentInfo.setUsername("1");
//        System.out.println(studentInfo.toString());
//        System.out.println(studentInfoDao.getStudentInfoAll(studentInfo));
//        System.out.println(studentUserDao.getUserInfo("1001"));
    }

    @Test
    void contextLoads() {
    }

    //数据生成
    @Test
    void getDataTest(){

        for (int i=4;i<50;i++){
            System.out.println("INSERT INTO `laboratory_system`.`laboratory_info`(`labid`, `building_name`, `capacity`, `category`, `describe`, `status`, `room_number`) " +
                    "VALUES ("+i+", '实验楼"+i+"', 50, '电脑', '实验用途"+i+"', '开发', '10"+i+"');");
        }

//        //公告表notice
//        for (int i = 10020; i <10040 ; i++) {
//            System.out.println("INSERT INTO `laboratory_system`.`notice`(`id`, `title`, `content`, `time`, `adminid`) " +
//                    "VALUES ("+i+", '公告测试"+i+"', '公告内容"+i+"', '2022-03-03', 10002);");
//        }

//        //tea_info表
//        for (int i = 10002; i <10050 ; i++) {
//            System.out.println("INSERT INTO `laboratory_system`.`tea_info`(`teaid`, `name`, `age`, `sex`, `tel`, `entry_date`, `address`, `college`, `position`, `birth`) " +
//                    "VALUES ("+i+", '李老师"+i+"', 18, '男', '130999"+i+"', '2022-03-24', '广东省广州市海珠区李家"+i+"', '信息学院', '老师', '2022-03-09');");
//        }

//        //admin_info表
//        for (int i = 10002; i <10050 ; i++) {
//            System.out.println("INSERT INTO `laboratory_system`.`admin_info`(`adminid`, `teaid`) " +
//                    "VALUES ("+i+", "+i+");");
//        }

//        //stu_info表
//        for (int i = 10020; i <10040 ; i++) {
//            System.out.println("INSERT INTO `laboratory_system`.`stu_info`(`stuid`, `name`, `age`, `sex`, `tel`, `studay_date`, `address`, `college`, `major`, `class`, `birth`) " +
//                    "VALUES ("+i+", '李四', 18, '女', '130888"+i+"', '2022-09-03', '广东省广州市海珠区李四', '信息学院', '软件工程', 'NET班级', '2022-03-17');" );
//        }

//        //stu_user表
//        for (int i=10050;i<10100;i++){
//            System.out.println("" +
//                    "INSERT INTO `laboratory_system`.`stu_user`(`stuid`, `username`, `password`, `status`, `create_time`, `email`, `role`)" +
//                    " VALUES ("+i+", '学生测试"+i+"', 'e02c1d7e805d6fab40b867d4d4197d6ff05ae1c442e0686b4778f9c54ec06fa75564264ec2b2eb18', '正常', now(), '"+i+"@qq.com', 'student');");
//        }

        //admin_user表
//        int j=1;
//        for (int i = 10003 ; i <10050 ; i++) {
//            if (j>29) j=1;else j++;
//            System.out.println("INSERT INTO `laboratory_system`.`admin_user`(`adminid`, `username`, `password`, `status`, `create_time`, `email`, `role`) " +
//                    "VALUES ("+i+", '管理测试"+i+"', 'e02c1d7e805d6fab40b867d4d4197d6ff05ae1c442e0686b4778f9c54ec06fa75564264ec2b2eb18', '正常', '2022-03-"+j+" 15:19:29', '"+i+"@email.com', 'admin');");
//        }

//        //tea_user表
//        int j=1;
//        for (int i = 10003 ; i <10050 ; i++) {
//            if (j>29) j=1;else j++;
//            System.out.println("INSERT INTO `laboratory_system`.`tea_user`(`teaid`, `username`, `password`, `status`, `create_time`, `email`, `role`) " +
//                    "VALUES ("+i+", '教师测试"+i+"', 'e02c1d7e805d6fab40b867d4d4197d6ff05ae1c442e0686b4778f9c54ec06fa75564264ec2b2eb18', '正常', '2022-03-"+j+" 15:28:28', '"+i+"@email', 'teacher');");
//        }
    }
}
