package com.javaproject.dianping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = {"com.javaproject.dianping"})
@MapperScan("com.javaproject.dianping.dal")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class RecommendedsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendedsystemApplication.class, args);
    }

}
