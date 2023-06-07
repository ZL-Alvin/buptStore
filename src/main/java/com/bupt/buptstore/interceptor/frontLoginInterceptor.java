package com.bupt.buptstore.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Title: frontLoginInterceptor
 * @Author Alvin
 * @Package com.bupt.buptstore.interceptor
 * @Date 2023/6/5 21:27
 * @description: 小程序登录拦截器
 */
public class frontLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId == null) {
            //说明用户没有登录成功，重定向到login.html
            response.sendRedirect("/front/page/login.html");
            //请求拦截
            return false;
        }
        //请求放行
        return true;
    }
}
