package com.bupt.buptstore.config;

import com.bupt.buptstore.common.CustomJsonHttpMessageConverter;
import com.bupt.buptstore.common.JacksonObjectMapper;
import com.bupt.buptstore.interceptor.frontLoginInterceptor;
import com.bupt.buptstore.interceptor.loginInterceptor;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
//@EnableSwagger2
//@EnableKnife4j
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
//        patterns.add("/doc.html");
//        patterns.add("/webjars/**");
//        patterns.add("/swagger-resources");
//        patterns.add("/v2/api-docs");
/*
* 拦截器无法拦截静态资源映射请求，因为在Spring MVC中，请求的处理是按照HandlerMapping和HandlerAdapter的顺序进行的，
* 而拦截器是在HandlerMapping之后、HandlerAdapter之前执行的。而静态资源处理器会在HandlerMapping中进行处理，
* 因此在静态资源请求中，拦截器还没有被执行。
* 可以使用Filter替代拦截器：可以通过自定义Filter来处理静态资源请求，并将其注册到Spring Boot应用程序中。Filter的执行顺序在拦截器之前，因此可以拦截到静态资源请求。
* 或者使用自定义HandlerInterceptorAdapter：继承HandlerInterceptorAdapter并实现preHandle()方法，
* 然后在该方法中对静态资源请求进行处理。将自定义的拦截器注册到Spring Boot应用程序中。
* */
        registry.addInterceptor(frontInterceptor).addPathPatterns("/front/**").excludePathPatterns(patterns);
        registry.addInterceptor(interceptor).addPathPatterns("/backend/**").excludePathPatterns(patterns);

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

    /*knife4j的配置类*/
//    @Bean
//    public Docket createRestApi() {
//        //文档类型
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//                .apis(RequestHandlerSelectors.basePackage("com.bupt.buptstore.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder().title("buptStore").version("1.0").description("buptStore接口文档").build();
//    }
//


//    静态资源映射配置
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    WebMvcConfigurer.super.addResourceHandlers(registry);
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        //addResourceHandler是映射的地址，addResourceLocations是静态资源地址的地址，classpath:是生成target的classes文件夹地址，file:是系统文件中的绝对路径地址
//        registry.addResourceHandler("/haha/**").addResourceLocations("classpath:/static/backend/");
//    }
}
