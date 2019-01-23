package com.qujiali.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Author YangDeLong 
 * @Description:
 * @Date 14:48 2018/12/19 0019
 * @Param  
 * @return 
 * **/

@SpringBootApplication
@MapperScan("com.qujiali.springboot.mapper")
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}

