package com.springdeveloper.demo;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloBootFunWebPojoApplication {

	Logger log = LoggerFactory.getLogger(HelloBootFunWebPojoApplication.class);

	@Bean
	public Function<Greeting, String> hello() {
		return g -> {
			log.info("GOT: " + g);
			return "hello " + g.getName() + " [" + g.getType() + "]";
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloBootFunWebPojoApplication.class, args);
	}

}
