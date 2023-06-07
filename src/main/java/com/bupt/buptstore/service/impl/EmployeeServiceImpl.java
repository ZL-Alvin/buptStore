package com.bupt.buptstore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.buptstore.mapper.EmployeeMapper;
import com.bupt.buptstore.pojo.Employee;
import com.bupt.buptstore.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Title: EmployeeServiceImpl
 * @Author Alvin
 * @Package com.bupt.buptstore.service.impl
 * @Date 2023/5/18 16:20
 * @description: Employee服务层实现类
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
