package com.springdeveloper.demo;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloBootFunWebfluxApplication {

	Logger log = LoggerFactory.getLogger("HelloBootFunWebfluxApplication");

	@Bean
	Function<Flux<Greeting>, Flux<String>> hello() {
		return in -> in.map(g -> greet(g));
	}

	private String greet(Greeting in) {
		log.info("GOT: " + in);
		return "hello " + in.getMessage() + " [" + in.getType() + "]";
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloBootFunWebfluxApplication.class, args);
	}

}
