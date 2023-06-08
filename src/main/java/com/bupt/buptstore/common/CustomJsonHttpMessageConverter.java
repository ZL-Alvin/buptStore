package com.bupt.buptstore.common;

/**
 * @Title: CustomJsonHttpMessageConverter
 * @Author Alvin
 * @Package com.bupt.buptstore.common
 * @Date 2023/6/8 20:36
 * @description:
 */
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;

public class CustomJsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public CustomJsonHttpMessageConverter() {
        super();
        // 添加"text/html;charset=UTF-8"到支持的媒体类型列表
        setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        // 初始化自定义的ObjectMapper
        setObjectMapper(new JacksonObjectMapper());
    }
}