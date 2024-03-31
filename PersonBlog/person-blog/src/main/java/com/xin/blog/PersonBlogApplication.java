package com.xin.blog;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2 // 开启Swagger接口管理
@EnableScheduling // 开启定时任务
@SpringBootApplication
@MapperScan("com.xin.blog.mapper")
public class PersonBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonBlogApplication.class,args);
    }
}
