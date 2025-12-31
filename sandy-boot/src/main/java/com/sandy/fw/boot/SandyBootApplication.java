package com.sandy.fw.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Sandy Framework 启动类
 * 聚合所有业务模块的统一入口
 * 
 * @ComponentScan 扫描 com.sandy.fw 包下所有组件，包括：
 * - sandy-common: 公共模块
 * - sandy-security: 安全模块
 * - sandy-system: 系统管理模块
 * - sandy-pay: 支付模块
 */
@SpringBootApplication
@ComponentScan("com.sandy.fw")
public class SandyBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SandyBootApplication.class, args);
    }
}
