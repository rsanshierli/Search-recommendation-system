package com.javaproject.dianping.common;


import com.javaproject.dianping.controller.admin.AdminController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Aspect // 表示是一个切面
@Configuration // 声明为其是一个bean
public class ControllerAspect {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    // 定义一个调用 adminController 之前的前置校验的方法
    // Around 环绕式切面编程，admin路径下有 RequestMapping 标记的才会有 切面，实现统一的管理方式控制对应的权限
    @Around("execution(* com.javaproject.dianping.controller.admin.*.*(..)) && " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object adminControllerBeforeValidation(ProceedingJoinPoint joinPoint) throws Throwable {

        // 将 joinPoint 的函数签名 强转成 MethodSignature，拿到调用方法
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        // 拿到 method 上的 annotation
        AdminPermission adminPermission = method.getAnnotation(AdminPermission.class);
        if (adminPermission == null) {
            // 公共方法，直接放行
            Object resultObject = joinPoint.proceed(); // 直接执行被切面的 方法
            return resultObject;
        }

        // 如果已经存在 adminPermission 标记
        // 判断当前管理员是否登入

        // 先通过 httpServletRequest 判断其是否有登入
        String email = (String) httpServletRequest.getSession().getAttribute(AdminController.CURRENT_ADMIN_SESSION);
        if (email == null) { // 如果没有登入
            if (adminPermission.produceType().equals("text/html")) {
                httpServletResponse.sendRedirect("/admin/admin/loginpage");
                return null;
            }else { // 如果不是 text/html， 比如是 application/json 返回 responsebody 的形式
                CommonError commonError = new CommonError(EmBusinessError.ADMIN_SHOULD_LOGIN);
                return CommonRes.create(commonError, "fail");
            }

        }else {
            // 公共方法，直接放行
            Object resultObject = joinPoint.proceed(); // 直接执行被切面的 方法
            return resultObject;
        }

    }
}
