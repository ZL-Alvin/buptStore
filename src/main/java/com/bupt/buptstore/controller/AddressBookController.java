package com.bupt.buptstore.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bupt.buptstore.common.BaseContext;
import com.bupt.buptstore.common.R;
import com.bupt.buptstore.pojo.AddressBook;
import com.bupt.buptstore.service.AddressBookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: AddressBookController
 * @Author Alvin
 * @Package com.bupt.buptstore.controller
 * @Date 2023/6/6 10:16
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody AddressBook addressBook) {
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(userId);
        BaseContext.setCurrentId(userId);
        addressBookService.save(addressBook);
        return R.success("保存地址成功！");
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, AddressBook::getUserId, userId);
        List<AddressBook> addressBooks = addressBookService.list(queryWrapper);
        return R.success(addressBooks);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getBack(@PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(userId);
        BaseContext.setCurrentId(userId);
        addressBookService.updateById(addressBook);
        return R.success("修改成功！");
    }

    @DeleteMapping
    public R<String> delete(HttpServletRequest request, Long ids) {
        Long userId = (Long) request.getSession().getAttribute("user");
        BaseContext.setCurrentId(userId);
        addressBookService.removeById(ids);
        return R.success("删除成功！");
    }

    @PutMapping("/default")
    public R<String> setDefault(HttpServletRequest request, @RequestBody AddressBook addressBook) {
        Long userId = (Long) request.getSession().getAttribute("user");
        BaseContext.setCurrentId(userId);
        //将该userId的所有地址，根据其用户id都设置为非默认，即isDefault为0
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(userId != null, AddressBook::getUserId, userId).set(AddressBook::getIsDefault, 0);
        addressBookService.update(wrapper);
        //根据地址id，设置对应地址idDefault为1
        Long addressBookId = addressBook.getId();
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(addressBookId != null, AddressBook::getId, addressBookId).set(AddressBook::getIsDefault, 1);
        addressBookService.update(updateWrapper);
        return R.success("修改成功！");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }
}
