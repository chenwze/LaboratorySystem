package com.gdufe.laboratorysystem.service.impl;

import com.gdufe.laboratorysystem.dao.AdminUserDao;
import com.gdufe.laboratorysystem.dao.CodeDao;
import com.gdufe.laboratorysystem.dao.StudentUserDao;
import com.gdufe.laboratorysystem.dao.TeacherUserDao;
//import com.gdufe.laboratorysystem.service.UserService;
import com.gdufe.laboratorysystem.entity.Email;
import com.gdufe.laboratorysystem.entity.StudentUser;
import com.gdufe.laboratorysystem.entity.User;
import com.gdufe.laboratorysystem.entity.VerificationCode;
//import com.gdufe.laboratorysystem.utils.UserInfo;
import com.gdufe.laboratorysystem.utils.VerCodeGenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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
    StudentUserDao studentUserDao;

    @Autowired
    TeacherUserDao teacherUserDao;

    @Autowired
    AdminUserDao adminUserDao;

    @Autowired
    private CodeDao codeDao;
    /**
     * 验证码
     */
    private String code;
    /**
     * 发送时间
     */
    private Date sendTime;

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userType=request.getParameter("usertype");
        String password=request.getParameter("password");

//        if (!isInteger(username) || userType==null ){
//            throw new BadCredentialsException("用户数据类型错误");
//        }

        //学生认证
        if (userType.equals("2")){
            System.out.println("学生用户进行登录验证---------------------");
            //根据用户名查找账号信息
            User studentUser = studentUserDao.getStudent(username);
            System.out.println("学生用户进行登录验证-----------");
            return loginDo(studentUser,username,password);
        }

        //教师认证
        if (userType.equals("1")){
            User user = teacherUserDao.getTeacher(username);
            return loginDo(user,username,password);
        }

        //管理员认证
        if (userType.equals("0")){
            User user = adminUserDao.getAdmin(username);
            return loginDo(user,username,password);
        }

        throw new BadCredentialsException("用户类型不存在");
    }

    //用户验证账号是否正常
    public UserDetails loginDo(User user,String username,String password){
        if (user==null || user.equals(null)){
            throw new BadCredentialsException("用户" + username + " 不存在");
        }

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        if(!encoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("用户密码错误。");
        }
        if (!user.getStatus().equals("正常")){
            throw new BadCredentialsException("用户:"+user.getStatus());
        }

        return user;
    }


//    /**
//     * 发送验证码到指定邮箱
//     * @param sender 发送地址
//     * @param mailSender spring自带
//     * @param receiver 接受地址
//     */
//    @Async
//    public Map getCode(String sender, JavaMailSenderImpl mailSender, String receiver) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setSubject("验证码");//设置邮件标题
//        code = VerCodeGenerateUtil.generateVerCode();
//        sendTime = new Date();
//        message.setText("尊敬的用户,您好:\n"
//                + "\n本次请求的邮件验证码为:" + code + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
//                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）"); //设置邮件正文
//        message.setFrom(sender);//发件人
//        message.setTo(receiver);//收件人
//        mailSender.send(message);//发送邮件
//        return null;
//    }

    //发邮件
    @Async("taskExecutor")
    public void sendEmail(VerificationCode code) throws MessagingException {
//        codeDao.addCode(code);
        Email email = new Email();
        email.setTo(code.getEmail());
        email.setSubject("用户找回密码");
        email.setMultipartFlag(false); //可选
//        email.setText("这是纯文本邮件测试");
        email.setText("尊敬的用户,您好:\n"
                + "\n实验室管理系统本次验证码为:" + code.getCode() + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,email.getMultipartFlag());
        helper.setFrom(from);
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        helper.setText(email.getText(),email.getMultipartFlag());
//        Map<String, Resource> attachmentMap=email.getAttachmentMap();
//        if(attachmentMap!=null){
//            Set<String> keySet=attachmentMap.keySet();
//            for (String key:keySet) {
//                if (key.equals("imageID")) {
//                    helper.addInline(key,attachmentMap.get(key));
//                } else {
//                    helper.addAttachment(key,attachmentMap.get(key));
//                }
//            }
//        }
        mailSender.send(message);
    }


    /***
     * 忘记密码，用户修改密码
     */
    public HashMap retrievePassword(String usertype,String username,String password ,String code ,String email){
        HashMap map =new HashMap();
        map.put("status","201");

        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(secret);
        password=encoder.encode(password);
        if (usertype.equals("2")){
            int i = studentUserDao.upPasswrodEmail(username, password, email);
            if (i !=1){
                map.put("msg","用户名或邮箱错误！！");
                return map;
            }
            map.put("staus","200");
            map.put("msg","修改成功！！");
            return map;
        }
        if (usertype.equals("1")){
            int i = teacherUserDao.upPasswrodEmail(username, password, email);
            if (i !=1){
                map.put("msg","用户名或邮箱错误！！");
                return map;
            }
            map.put("staus","200");
            map.put("msg","修改成功！！");
            return map;
        }
        if (usertype.equals("0")){
            int i = adminUserDao.upPasswrodEmail(username, password, email);
            if (i !=1){
                map.put("msg","用户名或邮箱错误！！");
                return map;
            }
            map.put("staus","200");
            map.put("msg","修改成功！！");
            return map;
        }
        map.put("msg","用户类型不存在！！");
        return map;
    }


    /**
     * 判断是否是int数值
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}