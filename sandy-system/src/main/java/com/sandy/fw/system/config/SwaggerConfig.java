package com.sandy.fw.system.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                //创建选择器，控制哪些接口被加入文�?
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sandy.fw.admin.controller"))
                .build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                //文档标题
                .title("管理系统接口文档")
                //文档描述
                .description("本文档描述了用户管理系统的接口定义")
                //文档版本
                .version("1.0")
                //文档联系�?
                .contact(new Contact("sandy", "", ""))
                //文档版权
                .license("sandy")
                //文档版权地址
                .licenseUrl("")
                .build();
    }
}
