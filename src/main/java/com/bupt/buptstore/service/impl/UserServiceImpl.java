package com.bupt.buptstore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.buptstore.mapper.UserMapper;
import com.bupt.buptstore.pojo.User;
import com.bupt.buptstore.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Title: UserServiceImpl
 * @Author Alvin
 * @Package com.bupt.buptstore.service.impl
 * @Date 2023/6/5 20:31
 * @description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
