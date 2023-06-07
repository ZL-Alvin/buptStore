package com.bupt.buptstore.ex;

/**
 * @Title: FileUploadException
 * @Author Alvin
 * @Package com.bupt.buptstore.ex
 * @Date 2023/5/23 17:14
 * @description: 文件上传错误
 */
public class FileUploadException extends RuntimeException{
    public FileUploadException(String msg) {
        super(msg);
    }
}
