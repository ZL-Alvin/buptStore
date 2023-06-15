package com.bupt.buptstore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.buptstore.common.BaseContext;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.dto.DishDto;
import com.bupt.buptstore.pojo.Category;
import com.bupt.buptstore.pojo.Dish;
import com.bupt.buptstore.pojo.DishFlavor;
import com.bupt.buptstore.service.CategoryService;
import com.bupt.buptstore.service.DishFlavorService;
import com.bupt.buptstore.service.DishService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Title: DishController
 * @Author Alvin
 * @Package com.bupt.buptstore.controller
 * @Date 2023/5/30 10:46
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody DishDto dishDto) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        dishService.saveWithFlavor(dishDto);
        //清理所有菜品的缓存
        Set keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        return R.success("保存成功！");
    }

    /**
     * 菜品信息查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<DishDto>> page(Integer page, Integer pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getSort);

        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        //对records进行处理
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据id查询分类的类名
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        //赋值
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable("id") Long id) {
        DishDto dishDto = dishService.getDtoWithId(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody DishDto dishDto) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        dishService.updateWithFlavor(dishDto);
        //清理对应菜品缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("更新成功啦！");
    }

    @PostMapping("/status/{dishStatus}")
    public R<String> updateStatus(HttpServletRequest request, @RequestParam List<Long> ids, @PathVariable("dishStatus") Integer status) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        dishService.updateStatus(ids, status);
        return R.success("修改售卖状态成功！");
    }

    @DeleteMapping
    public R<String > deleteDishAndFlavor(HttpServletRequest request, @RequestParam List<Long> ids) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        dishService.deleteDishAndFlavor(ids);
        return R.success("删除成功！");
    }

    @GetMapping("/list")
    public R<List<DishDto>> getDishList(Dish dish) {
        List<DishDto> dtoList = null;
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        //从Redis中获取缓存数据
        dtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dtoList != null) {
            //如果存在直接返回
            return R.success(dtoList);
        }
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                .eq(dish.getName() != null, Dish::getName, dish.getName()).eq(Dish::getStatus, 1);
        List<Dish> dishes = dishService.list(queryWrapper);
        dtoList = dishes.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(item.getId() != null, DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis中
        redisTemplate.opsForValue().set(key, dtoList);
        return R.success(dtoList);
    }
}
