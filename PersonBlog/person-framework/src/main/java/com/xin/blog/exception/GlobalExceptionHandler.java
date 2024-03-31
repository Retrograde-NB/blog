package com.xin.blog.exception;

import com.xin.blog.res.AppHttpCodeEnum;
import com.xin.blog.res.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e,  HttpServletRequest request){
        //打印异常信息
        log.error("请求地址：{} ", request.getRequestURI());
        log.error("出现了异常！ {}",e.getMessage());
        log.error(e.toString());
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }


    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e, HttpServletRequest request){
        //打印异常信息
        log.error("请求地址：{} ", request.getRequestURI());
        log.error("出现了异常！ {}",e.getMessage());
        log.error(e.toString());

        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}