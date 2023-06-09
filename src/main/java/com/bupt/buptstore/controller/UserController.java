package com.bupt.buptstore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.pojo.User;
import com.bupt.buptstore.service.UserService;
import com.bupt.buptstore.utils.MailUtils;
import com.bupt.buptstore.utils.SMSUtils;
import com.bupt.buptstore.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Title: UserController
 * @Author Alvin
 * @Package com.bupt.buptstore.controller
 * @Date 2023/6/5 20:32
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        String email = user.getEmail();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机的4位验证码
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            System.err.println(code);

            //调用阿里云提供的短信服务API完成发送短信
//            SMSUtils.sendMessage("外卖", "", phone, code);
            //调用邮箱smtp功能发送邮件验证码
            MailUtils.sendMail(email, code);
            //将生成的验证码保存到Session
            session.setAttribute(phone, code);
            return R.success("手机验证码发送成功！");
        }
        return R.error("手机验证码发送失败!");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功，说明登陆成功
            //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                user.setName(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败！");
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("退出登录成功！");
    }
}
