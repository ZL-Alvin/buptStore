package com.bupt.buptstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bupt.buptstore.dto.SetmealDto;
import com.bupt.buptstore.mapper.SetmealMapper;
import com.bupt.buptstore.pojo.Setmeal;
import com.bupt.buptstore.pojo.SetmealDish;
import com.bupt.buptstore.service.SetmealDishService;
import com.bupt.buptstore.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: SetmealServiceImpl
 * @Author Alvin
 * @Package com.bupt.buptstore.service.impl
 * @Date 2023/5/22 21:21
 * @description:
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveMealAndDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void updateStatus(List<Long> ids, Integer status) {
        List<Setmeal> setmealList = ids.stream().map(item -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(item);
            if (status == 0) {
                setmeal.setStatus(0);
            } else {
                setmeal.setStatus(1);
            }
            return setmeal;
        }).collect(Collectors.toList());
        this.updateBatchById(setmealList);
    }

    @Override
    public SetmealDto getDtoById(Long id) {
        Setmeal setmeal = this.getById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        //清空套餐菜品
        Long setmealId = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        setmealDishService.remove(queryWrapper);

        //插入套餐菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteSetmealAndmealDishById(List<Long> ids) {
        this.removeBatchByIds(ids);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null, SetmealDish::getSetmealId, ids);
        setmealDishService.remove(queryWrapper);
    }
}
