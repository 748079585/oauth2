package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author lei
 * @date 2019/08/16
 */
@SpringBootApplication
@EnableResourceServer
//@EnableEurekaClient
public class EurekaAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaAuthApplication.class, args);
	}

}
