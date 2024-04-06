package com.mai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
@SpringBootApplication是一个复合注解，包括了
@Configuration、@EnableAutoConfiguration 和 @ComponentScan 三个注解，
用于标识 Spring Boot 应用的主类。

@Configuration：表明该类是一个 Java 配置类，用于定义 Spring Bean 的配置信息。
@EnableAutoConfiguration：自动配置注解，用于开启 Spring Boot 的自动配置功能。
@ComponentScan：用于自动扫描和注册 Spring Bean。
 */
// @MapperScan(basePackages = "com.mai.mapper")
@SpringBootApplication
public class MaiApplication {

    // private static int port;
    // private static int fz;
    // private static Integer port2;

    public static void main(String[] args) {
        SpringApplication.run(MaiApplication.class, args);
        // System.out.println("当前端口号===>"+port);
        // System.out.println("学号===>" + fz);
    }

    // @Value("${server.port}")
    // public void getPort(int ymlPort){
    //     MaiApplication.port = ymlPort;
    // }
    //
    // @Value("${my.fz}")
    // public void getFz(int ymlFz) {
    //     MaiApplication.fz = ymlFz;
    // }

}
