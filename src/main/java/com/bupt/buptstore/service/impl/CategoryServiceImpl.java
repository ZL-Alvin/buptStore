package com.bupt.buptstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.buptstore.ex.CustomException;
import com.bupt.buptstore.mapper.CategoryMapper;
import com.bupt.buptstore.pojo.Category;
import com.bupt.buptstore.pojo.Dish;
import com.bupt.buptstore.pojo.Setmeal;
import com.bupt.buptstore.service.CategoryService;
import com.bupt.buptstore.service.DishService;
import com.bupt.buptstore.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Title: CategoryServiceImpl
 * @Author Alvin
 * @Package com.bupt.buptstore.service.impl
 * @Date 2023/5/22 19:04
 * @description:
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联，则抛出一个业务异常
        if (count1 > 0) {
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }
        //查询当前分类是否关联了套餐，如果已经关联，则抛出一个异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //已经关联套餐，抛出一个业务异常
        }
        //正常删除
        super.removeById(id);
    }
}
