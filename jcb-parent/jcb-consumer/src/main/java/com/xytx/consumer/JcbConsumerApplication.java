package com.xytx.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xytx.consumer.mapper")
public class JcbConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JcbConsumerApplication.class, args);
    }

}
