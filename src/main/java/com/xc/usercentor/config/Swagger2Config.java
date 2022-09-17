package com.xc.usercentor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {

    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
//扫描路径，获取controller层的接口    .apis(RequestHandlerSelectors.basePackage("com.xc.usercentor.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
            //标题
                .title("xc用户中心")
            //简介
                .description("xc用户中心接口文档")
            //作者、网址http:localhost:8088/doc.html(这里注意端口号要与项目一致，如果你的端口号后面还加了前缀，就需要把前缀加上)、邮箱
                .contact(new Contact("xc","https://github.com/xc66xc","2507849932@qq.com"))
            //版本
                .version("1.0")
                .build();
    }
}

