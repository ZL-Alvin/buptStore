package com.bupt.buptstore.controller;

import com.bupt.buptstore.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: OrderDetailController
 * @Author Alvin
 * @Package com.bupt.buptstore.controller
 * @Date 2023/6/6 21:26
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/orderDetail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;


}
