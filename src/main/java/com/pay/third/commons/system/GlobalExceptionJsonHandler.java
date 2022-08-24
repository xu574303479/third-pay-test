package com.pay.third.commons.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 统一异常处理
 *
 * @author Tang
 * @version 1.0.0
 * @date 2021/3/30 20:27
 */
@ControllerAdvice
public class GlobalExceptionJsonHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionJsonHandler.class);

    /**
     * 捕捉系统Exception异常
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ResponseBody // 输出Json
    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        LOG.error("错误代码：{}，错误信息：{}", 500, e.getMessage());
        e.printStackTrace();
        return Result.unknow();
    }

    /**
     * 捕捉自定义异常
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(value = CustomException.class)
    public Result defaultErrorHandler(HttpServletRequest req, CustomException e) throws Exception {
        LOG.error("错误代码：{}，子错误代码：{}，错误信息：{}", e.getCode(), e.getSubCode(), e.getMessage());
        e.printStackTrace();
        return Result.error().msg(e.getMessage());
    }

    /**
     * 捕捉实体参数校验异常
     *
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result defaultErrorHandler(HttpServletRequest req, MethodArgumentNotValidException e) throws Exception {
//        String errorMsg = "";
//
//        List<FieldError> errors = e.getBindingResult().getFieldErrors();
//        for (FieldError error : errors) {
//            errorMsg += error.getField() + ":" + error.getDefaultMessage();
//        }

        String errorMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        LOG.error("实体参数校验异常，错误代码：{}，错误信息：{}", 1, e.getMessage());
        e.printStackTrace();
        return Result.error().msg(errorMsg);
    }

    /**
     * 捕获单个参数校验异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result defaultErrorHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        String errorMsg = "";

        for (ConstraintViolation<?> item : set) {
            errorMsg += item.getMessage() + ";";
        }

        LOG.error("单个参数校验异常，错误代码：{}，错误信息：{}", 1, e.getMessage());
        e.printStackTrace();
        return Result.error().msg(errorMsg);
    }
}
