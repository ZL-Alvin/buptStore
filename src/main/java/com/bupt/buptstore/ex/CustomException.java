package com.bupt.buptstore.ex;

/**
 * @Title: CustomException
 * @Author Alvin
 * @Package com.bupt.buptstore.ex
 * @Date 2023/5/23 17:10
 * @description: 自定义异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String mes) {
        super(mes);
    }
}