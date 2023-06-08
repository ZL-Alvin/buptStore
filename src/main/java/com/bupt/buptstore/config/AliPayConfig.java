package com.bupt.buptstore.config;

/**
 * @Title: AlipayConfig
 * @Author Alvin
 * @Package com.bupt.buptstore.config
 * @Date 2023/6/8 17:13
 * @description:
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    private String url;
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String returnUrl;
}