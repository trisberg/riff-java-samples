package functions;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
public class HelloMessageApplication {

	@Bean
	public Function<Message<String>, Message<String>> hello() {
		return (in) -> {
			System.out.println("HEADERS: " + in.getHeaders());
			Object type = in.getHeaders().get("ce-type");
			Object id = in.getHeaders().get("ce-id");
			return MessageBuilder.withPayload("Processed " + type + ":" + id).build();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(HelloMessageApplication.class, args);
	}

}
