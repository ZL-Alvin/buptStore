package com.bupt.buptstore.interceptor;

import com.bupt.buptstore.common.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Title: webMVCConfig
 * @Author Alvin
 * @Package com.bupt.buptstore.intercepter
 * @Date 2023/5/19 10:14
 * @description: 拦截器
 */
public class loginInterceptor implements HandlerInterceptor {
    /**
     * 检测全局Session中是否有employee，如果有则放行，如果没有则重定向到登录界面
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器（url+controller：映射）
     * @return 如果返回true则放行，如果返回false则表示拦截当前请求
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long empId = (Long)request.getSession().getAttribute("employee");
        if (empId == null) {
            //说明用户没有登录成功，重定向到login.html页面
            response.sendRedirect("/backend/page/login/login.html");
            //拦截请求
            return false;
        }
        //请求放行
        return true;
    }
}
