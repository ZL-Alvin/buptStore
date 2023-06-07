package com.bupt.buptstore.common;

import com.bupt.buptstore.ex.CustomException;
import com.bupt.buptstore.ex.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Title: GlobalExceptionHandler
 * @Author Alvin
 * @Package com.bupt.buptstore.common
 * @Date 2023/5/19 13:33
 * @description: 全局异常处理
 */
//@ControllerAdvice(annotations = {RestController.class, Controller.class})
//@ResponseBody
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(Throwable ex) {
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        return R.error(ex.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    public R<String> exceptionHandler(FileUploadException ex) {
        return R.error(ex.getMessage());
    }
}
