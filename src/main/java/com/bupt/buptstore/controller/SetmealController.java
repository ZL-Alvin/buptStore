package com.bupt.buptstore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.buptstore.common.BaseContext;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.dto.SetmealDto;
import com.bupt.buptstore.pojo.Category;
import com.bupt.buptstore.pojo.Setmeal;
import com.bupt.buptstore.pojo.SetmealDish;
import com.bupt.buptstore.service.CategoryService;
import com.bupt.buptstore.service.SetmealDishService;
import com.bupt.buptstore.service.SetmealService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: SetmealController
 * @Author Alvin
 * @Package com.bupt.buptstore.controller
 * @Date 2023/6/5 15:33
 * @description: 套餐管理的控制层
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody SetmealDto setmealDto) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        setmealService.saveMealAndDish(setmealDto);
        return R.success("添加套餐成功！");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name) {
        //获取Setmeal的pageInfo
        Page<Setmeal> setmealPageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name != null, Setmeal::getName, name);
        setmealService.page(setmealPageInfo, queryWrapper);

        //对Setmeal的pageInfo的records列表进行补充数据，并将其转换成SetmealDto的列表
        List<Setmeal> records = setmealPageInfo.getRecords();
        List<SetmealDto> dtoRecords = records.stream().map(item -> {
            //拷贝数据
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            //查找categoryName
            Category category = categoryService.getById(item.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        //创建Page<SetmealDto>
        Page<SetmealDto> dtoPageInfo = new Page<>(page, pageSize);
        BeanUtils.copyProperties(setmealPageInfo, dtoPageInfo, "records");
        dtoPageInfo.setRecords(dtoRecords);
        return R.success(dtoPageInfo);
    }

    @PostMapping("/status/{setmealStatus}")
    public R<String> updateStatus(HttpServletRequest request, @RequestParam List<Long> ids, @PathVariable("setmealStatus") Integer status) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        setmealService.updateStatus(ids, status);
        return R.success("修改成功！");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealById(@PathVariable("id") Long id) {
        SetmealDto setmealDto = setmealService.getDtoById(id);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody SetmealDto setmealDto) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        setmealService.updateSetmeal(setmealDto);
        return R.success("修改套餐信息成功！");
    }

    @DeleteMapping
    public R<String> delete(HttpServletRequest request, @RequestParam List<Long> ids) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        setmealService.deleteSetmealAndmealDishById(ids);
        return R.success("删除成功！");
    }

    @Cacheable(value = "setmealCache", key = "#categoryId + '_' + #status")
    @GetMapping("/list")
    public R<List<SetmealDto>> list(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId)
                .eq(status != null, Setmeal::getStatus, status);
        List<Setmeal> setmeals = setmealService.list(queryWrapper);
        List<SetmealDto> dtoList = setmeals.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealDish::getDishId, item.getId());
            List<SetmealDish> setmealDishes = setmealDishService.list(wrapper);
            setmealDto.setSetmealDishes(setmealDishes);
            return setmealDto;
        }).collect(Collectors.toList());
        return R.success(dtoList);
    }
}
