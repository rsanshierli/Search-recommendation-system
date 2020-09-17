package com.javaproject.dianping.common;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonRes doError(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Exception exception) {
        if (exception instanceof BusinessException) {  // BusinessException 错误，也就是定义的getCommonError错误，请求对象不存在错误
            return CommonRes.create(((BusinessException)exception).getCommonError(), "fail");

        }else if (exception instanceof NoHandlerFoundException) {  // 4XX请求，服务器请求错误，无法找到Controller，比如get请求 查询时 错误变为 getx请求
            CommonError commonError = new CommonError(EmBusinessError.NO_HANDLER_FOUND);
            return CommonRes.create(commonError, "fail");

        }else if (exception instanceof ServletRequestBindingException) {  // 请求参数错误 查询id 请求为 查询 idt
            CommonError commonError = new CommonError(EmBusinessError.BIND_EXCEPTION);
            return CommonRes.create(commonError, "fail");

        }else {  // 其他错误，定义一个通用error “未知错误”，避免程序内的 空指针 或者 其他异常 传递给前端
            CommonError commonError = new CommonError(EmBusinessError.UNKNOWN_ERROR);
            return CommonRes.create(commonError, "fail");
        }
    }
}
