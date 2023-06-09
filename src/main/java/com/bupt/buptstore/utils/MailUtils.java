package com.bupt.buptstore.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Title: MailUtils
 * @Author Alvin
 * @Package com.bupt.buptstore.utils
 * @Date 2023/6/9 9:52
 * @description: 发送邮件验证码工具
 */
public class MailUtils {
    private static final String USER = "12_xianren@sina.com"; // 发件人称号，同邮箱地址※
    private static final String PASSWORD = "17d22a9de315fba0"; // 授权码，开启SMTP时显示※

    /**
     *
     * @param to 收件人邮箱
     * @param text 邮件正文
     */
    /* 发送验证信息的邮件 */
    public static boolean sendMail(String to, String text){
        try {
            final Properties props = new Properties();
            props.put("mail.smtp.auth", "true"); //开启认证
//            注意发送邮件的方法中，发送给谁的，发送给对应的app，※
//            要改成对应的app。扣扣的改成qq的，网易的要改成网易的。※
//            props.put("mail.smtp.host", "smtp.qq.com");
            props.put("mail.smtp.host", "smtp.sina.com");

/*            阿里云ECS默认禁用25端口导致发邮件失败：Couldn't connect to host, port: smtp.example.com
            有两种解决方法：
                        1. 向阿里云申请解封25端口  => https://help.aliyun.com/knowledge_detail/56130.html
                        2. 改用465端口ssl加密发送。*/
            // 因为阿里云服务器不允许smtp（默认端口25）发送邮件，所以要改成ssl发送，并且设置端口为465，本机测试的话不加下面这段代码也可也成功
            props.put("mail.smtp.port","465");//设置端口
            props.put("mail.smtp.ssl.enable", true);
            props.put("mail.smtp.socketFactory.port", "465");//设置ssl端口
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            //*************************************************************************************
            // 发件人的账号
            props.put("mail.user", USER);
            //发件人的密码
            props.put("mail.password", PASSWORD);

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            String username = props.getProperty("mail.user");
            InternetAddress form = new InternetAddress(username);
            message.setFrom(form);

            // 设置收件人
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            // 设置邮件标题
            message.setSubject("小朱杂货铺的验证码");

            // 设置邮件的内容体
            message.setContent(text, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception { // 做测试用
        MailUtils.sendMail("799257860@qq.com","你好，这是一封测试邮件，无需回复。");//填写接收邮箱※
        System.out.println("发送成功");
    }
}
