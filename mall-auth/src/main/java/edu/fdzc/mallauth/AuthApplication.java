package edu.fdzc.mallauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class)
@EnableDiscoveryClient
@MapperScan("edu.fdzc.mallauth.mapper")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
