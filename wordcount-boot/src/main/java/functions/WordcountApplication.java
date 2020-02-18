package functions;

import java.util.Map;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class WordcountApplication {

	@Bean
	Function<Flux<String>, Flux<Map<String, Integer>>> wordcount() {
		return new WordcountFunction();
	}

	public static void main(String[] args) {
		SpringApplication.run(WordcountApplication.class, args);
	}
}
