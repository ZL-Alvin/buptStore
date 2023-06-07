package com.bupt.buptstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.buptstore.dto.DishDto;
import com.bupt.buptstore.mapper.DishMapper;
import com.bupt.buptstore.pojo.Dish;
import com.bupt.buptstore.pojo.DishFlavor;
import com.bupt.buptstore.service.DishFlavorService;
import com.bupt.buptstore.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: DishServiceImpl
 * @Author Alvin
 * @Package com.bupt.buptstore.service.impl
 * @Date 2023/5/22 21:20
 * @description:
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 添加菜品的同时添加口味，操作Dish和DishFlavor两张表
     * @param dishDto 数据
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品信息
        this.save(dishDto);
        //保存口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        //在保存菜品信息的时候，mp底层会用雪花算法先生成id放入dishDto对象中，再插入到数据库中，不是数据库自增的方式
        Long dishId = dishDto.getId();
        flavors = flavors.stream().map(t -> {
           t.setDishId(dishId);
           return t;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Transactional
    @Override
    public DishDto getDtoWithId(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味数据--dish_flavor表的Delete操作
        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dishId != null, DishFlavor::getDishId, dishId);
        dishFlavorService.remove(queryWrapper);

        //添加当前口味信息dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateStatus(List<Long> ids, Integer status) {
        List<Dish> dishes = ids.stream().map(t -> {
            Dish dish = new Dish();
            dish.setId(t);
            if (status == 0) {
                dish.setStatus(0);
            }
            else {
                dish.setStatus(1);
            }
            return dish;
        }).collect(Collectors.toList());
        this.updateBatchById(dishes);
    }

    @Override
    public void deleteDishAndFlavor(List<Long> ids) {
        this.removeBatchByIds(ids);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null, DishFlavor::getDishId, ids);
        dishFlavorService.remove(queryWrapper);
    }

}
