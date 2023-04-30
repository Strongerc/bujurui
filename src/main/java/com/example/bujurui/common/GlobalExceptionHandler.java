package com.example.bujurui.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */

/*
* 全局异常处理代理
* annotations = {RestController.class, Controller.class} 拦截加了这个注解的controller
* ResponseBody 将结果封装成json数据返回
* @ExceptionHandler(SQLIntegrityConstraintViolationException.class)  处理异常的类型SQLIntegrityConstrain
*  处理 相同sql的异常 同名添加
* if(ex.getMessage().contains("Duplicate entry")) 这是上面异常包含的信息 contains包含关键字
* split[2] 拿到这个 异常名字 ‘张三’
*
 *
*
*
* */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }

}
