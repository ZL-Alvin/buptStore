package com.bupt.buptstore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.buptstore.mapper.AddressBookMapper;
import com.bupt.buptstore.pojo.AddressBook;
import com.bupt.buptstore.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @Title: AddressBookServiceImpl
 * @Author Alvin
 * @Package com.bupt.buptstore.service.impl
 * @Date 2023/6/6 10:12
 * @description:
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
