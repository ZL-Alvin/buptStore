package com.bupt.buptstore.config;

import com.bupt.buptstore.common.CustomJsonHttpMessageConverter;
import com.bupt.buptstore.common.JacksonObjectMapper;
import com.bupt.buptstore.interceptor.frontLoginInterceptor;
import com.bupt.buptstore.interceptor.loginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: webMVCConfig
 * @Author Alvin
 * @Package com.bupt.buptstore.config
 * @Date 2023/5/19 10:24
 * @description:
 */
@Configuration
public class webMVCConfig implements WebMvcConfigurer {
    //配置登录拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor interceptor = new loginInterceptor();
        HandlerInterceptor frontInterceptor = new frontLoginInterceptor();
        ArrayList<String> patterns = new ArrayList<>();
        patterns.add("/backend/api/**");
        patterns.add("/backend/images/**");
        patterns.add("/backend/js/**");
        patterns.add("/backend/plugins/**");
        patterns.add("/backend/styles/**");
        patterns.add("/backend/page/login/**");
        patterns.add("/backend/favicon.ico");
        patterns.add("/front/api/**");
        patterns.add("/front/fonts/**");
        patterns.add("/front/images/**");
        patterns.add("/front/js/**");
        patterns.add("/front/styles/**");
        patterns.add("/front/page/login.html");
        patterns.add("/user/sendMsg");
        patterns.add("/user/login");
        patterns.add("/alipay/**");
        registry.addInterceptor(interceptor).addPathPatterns("/backend/**").excludePathPatterns(patterns);
        registry.addInterceptor(frontInterceptor).addPathPatterns("/front/**").excludePathPatterns(patterns);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换起集合中
        converters.add(0, messageConverter);
        converters.add(new CustomJsonHttpMessageConverter());
    }
}
