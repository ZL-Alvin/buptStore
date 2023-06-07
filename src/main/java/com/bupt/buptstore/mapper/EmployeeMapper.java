package com.bupt.buptstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.buptstore.pojo.Employee;
import org.springframework.stereotype.Repository;

/**
 * @Title: EmployeeMapper
 * @Author Alvin
 * @Package com.bupt.buptstore.mapper
 * @Date 2023/5/18 16:15
 * @description: Employee表的dao层
 */
@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {
}
