package com.bupt.buptstore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.buptstore.mapper.ShoppingCartMapper;
import com.bupt.buptstore.pojo.ShoppingCart;
import com.bupt.buptstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @Title: ShoppingCartServiceImpl
 * @Author Alvin
 * @Package com.bupt.buptstore.service.impl
 * @Date 2023/6/6 18:43
 * @description:
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
