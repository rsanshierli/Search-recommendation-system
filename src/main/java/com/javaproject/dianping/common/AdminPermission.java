package com.javaproject.dianping.common;


import javax.swing.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) // 一般如果需要在运行时去动态获取注解信息，那只能用 RUNTIME 注解
public @interface AdminPermission {

    String produceType() default "text/html";
}
