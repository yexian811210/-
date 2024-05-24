package com.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 启动类
 */
@MapperScan("com.manager.mapper") //加载Mapper接口
@SpringBootApplication
@EnableSwagger2
public class WangTushuApplication {

    public static void main(String[] args) {
        SpringApplication.run(WangTushuApplication.class, args);
    }

}
