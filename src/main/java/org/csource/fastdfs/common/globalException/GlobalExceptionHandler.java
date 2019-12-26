package org.csource.fastdfs.common.globalException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liujiakuan on 2019/11/2.
 */
//该注解定义全局异常处理类
@ControllerAdvice
//@ControllerAdvice(basePackages ="com.example.demo.controller") 可指定包
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //该注解声明异常处理方法
    @ExceptionHandler(value = Exception.class)
    public ExceptionResult exceptionHandler(Exception e) {
        logger.error("",e);
        //对于自定义异常的处理
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return ExceptionResult.error(ex.getExceptionMessage(), ex.toString(), Arrays.toString(ex.getStackTrace()));
            //对于绑定异常的处理，使用jsr303中的自定义注解抛出的异常属于绑定异常
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            logger.info("==Exception Message=={}", msg);
            return ExceptionResult.error(CodeMsg.BIND_EXCEPTION, e.toString(), Arrays.toString(e.getStackTrace()));
        } else {
            return ExceptionResult.error(CodeMsg.SERVER_EXCEPTION, e.toString(), Arrays.toString(e.getStackTrace()));
        }
    }
}
