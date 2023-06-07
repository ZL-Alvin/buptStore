package com.bupt.buptstore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.buptstore.common.BaseContext;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.pojo.Category;
import com.bupt.buptstore.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: CategoryController
 * @Author Alvin
 * @Package com.bupt.buptstore.controller
 * @Date 2023/5/22 19:06
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Category category) {
        Long empId = (Long)request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        categoryService.save(category);
        return R.success("添加成功！");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(HttpServletRequest request, Long ids) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        //调用自定义删除方法
        categoryService.remove(ids);
        return R.success("删除成功！");
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Category category) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        categoryService.updateById(category);
        return R.success("修改成功！");
    }

    @GetMapping("/list")
    public R<List<Category>> getCategoryList(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
