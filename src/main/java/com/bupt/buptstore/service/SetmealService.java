package com.bupt.buptstore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.buptstore.dto.SetmealDto;
import com.bupt.buptstore.pojo.Setmeal;

import java.util.List;

/**
 * @Title: SetmealService
 * @Author Alvin
 * @Package com.bupt.buptstore.service
 * @Date 2023/5/22 21:22
 * @description:
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 保存套餐信息和套餐菜品信息
     * @param setmealDto
     */
    public void saveMealAndDish(SetmealDto setmealDto);

    /**
     * 修改套餐状态
     * @param ids
     */
    public void updateStatus(List<Long> ids, Integer status);

    /**
     * 根据id查询dto信息
     * @param id
     * @return
     */
    public SetmealDto getDtoById(Long id);

    /**
     * 修改套餐信息
     * @param setmealDto
     */
    public void updateSetmeal(SetmealDto setmealDto);

    /**
     * 删除套餐及套餐中的菜品
     * @param ids
     */
    public void deleteSetmealAndmealDishById(List<Long> ids);
}
