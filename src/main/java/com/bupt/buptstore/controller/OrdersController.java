package com.bupt.buptstore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.dto.OrdersDto;
import com.bupt.buptstore.pojo.OrderDetail;
import com.bupt.buptstore.pojo.Orders;
import com.bupt.buptstore.service.OrderDetailService;
import com.bupt.buptstore.service.OrdersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: OrdersController
 * @Author Alvin
 * @Package com.bupt.buptstore.controller
 * @Date 2023/6/6 21:22
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<Orders> submit(HttpServletRequest request, @RequestBody Orders orders) {
        Long userId = (Long) request.getSession().getAttribute("user");
        Orders submitOrder = ordersService.submit(userId, orders);
        return R.success(submitOrder);
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> frontPage(HttpServletRequest request, Integer page, Integer pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = (Long) request.getSession().getAttribute("user");
        queryWrapper.eq(userId != null, Orders::getUserId, userId).orderByDesc(Orders::getCheckoutTime);
        ordersService.page(ordersPage, queryWrapper);
        BeanUtils.copyProperties(ordersPage, dtoPage, "records");
        List<Orders> ordersPageRecords = ordersPage.getRecords();
        List<OrdersDto> dtoRecords = ordersPageRecords.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetailList = orderDetailService.list(wrapper);
            ordersDto.setOrderDetails(orderDetailList);
            return ordersDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(dtoRecords);
        return R.success(dtoPage);
    }

    @GetMapping("/page")
    public R<Page<OrdersDto>> backPage(HttpServletRequest request, Integer page, Integer pageSize, Long number, String beginTime, String endTime) {
        Long userId = (Long) request.getSession().getAttribute("user");
        Page<OrdersDto> pageInfo = ordersService.getBackPage(userId, page, pageSize, number, beginTime, endTime);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getId, orders.getId()).set(Orders::getStatus, orders.getStatus());
        ordersService.update(updateWrapper);
        return R.success("订单状态修改成功！");
    }

}
