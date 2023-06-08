package com.bupt.buptstore.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.config.AliPayConfig;
import com.bupt.buptstore.pojo.Orders;
import com.bupt.buptstore.service.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: AliPayController
 * @Author Alvin
 * @Package com.bupt.buptstore.controller
 * @Date 2023/6/8 17:37
 * @description:
 */
@RestController
@RequestMapping("/alipay")
public class AliPayController {
    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(Orders order, HttpServletResponse httpResponse) {
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfig.getUrl(), aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), "json", "utf-8", aliPayConfig.getAlipayPublicKey(), "RSA2");

        // 2. 创建 Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", String.valueOf(order.getId()));  // 我们自己生成的订单编号
        bizContent.set("total_amount", String.valueOf(order.getAmount())); // 订单的总金额
        bizContent.set("subject", "您选择的快乐！");   // 支付的名称
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());
        // 执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + "UTF-8");
        try {
            httpResponse.getOutputStream().write(form.getBytes());// 直接将完整的表单html输出到页面
            httpResponse.getOutputStream().flush();
            httpResponse.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                // System.out.println(name + " = " + request.getParameter(name));
            }

            String outTradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
            // 支付宝验签
            if (checkSignature) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 查询订单
//                LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//                queryWrapper.eq(Orders::getId, outTradeNo);
//                Orders orders = ordersService.getOne(queryWrapper);
//
//                if (orders != null) {
//                    orders.setAlipayNo(alipayTradeNo);
//                    orders.setPayTime(new Date());
//                    orders.setState("已支付");
//                    ordersMapper.updateById(orders);
//                }
            }
        }
        return "success";
    }
}
