package com.bupt.buptstore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bupt.buptstore.pojo.Category;

/**
 * @Title: CategoryService
 * @Author Alvin
 * @Package com.bupt.buptstore.service
 * @Date 2023/5/22 19:03
 * @description:
 */
public interface CategoryService extends IService<Category> {
    /**
     * 根据id删除分类，但是要进行是否有套餐或者菜品关联的判断，如果没有才能删除，否则抛出异常
     * @param id
     */
    public void remove(Long id);
}
