package com.bupt.buptstore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.dto.DishDto;
import com.bupt.buptstore.pojo.Dish;

import java.util.List;

/**
 * @Title: DishService
 * @Author Alvin
 * @Package com.bupt.buptstore.service
 * @Date 2023/5/22 21:19
 * @description:
 */
public interface DishService extends IService<Dish> {
    /**
     * 添加菜品信息和口味信息
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询dish和dishFlavor信息
     * @param id
     * @return
     */
    public DishDto getDtoWithId(Long id);

    /**
     * 修改菜品信息和口味信息
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);

    /**
     * 更改售卖状态
     * @param ids
     * @param status
     */
    public void updateStatus(List<Long> ids, Integer status);

    /**
     * 删除或者批量删除菜品信息及口味
     * @param ids
     */
    public void deleteDishAndFlavor(List<Long> ids);
}
