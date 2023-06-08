package com.bupt.buptstore.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.dto.OrdersDto;
import com.bupt.buptstore.pojo.Orders;

import java.time.LocalDateTime;

/**
 * @Title: OrdersService
 * @Author Alvin
 * @Package com.bupt.buptstore.service
 * @Date 2023/6/6 21:19
 * @description:
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * @param userId
     * @param orders
     * @return
     */
    public Orders submit(Long userId, Orders orders);

    /**
     * 后台订单明细显示
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    public Page<OrdersDto> getBackPage(Long userId, Integer page, Integer pageSize, Long number, String beginTime, String endTime);
}
