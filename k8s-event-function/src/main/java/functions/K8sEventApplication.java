package functions;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salaboy.cloudevents.helper.CloudEventsHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import io.cloudevents.CloudEvent;
import io.cloudevents.v03.AttributesImpl;

@SpringBootApplication
public class K8sEventApplication {

	Logger log = LoggerFactory.getLogger(K8sEventApplication.class);

	ObjectMapper mapper = new ObjectMapper();

	@Bean
	public Function<Message<JsonNode>, Message<String>> hello() {
		return (in) -> {
			Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
			for (String key : in.getHeaders().keySet()) {
				if (key.toLowerCase().startsWith("ce-")) {
					headers.put(key, in.getHeaders().get(key).toString());
				}
			}
			CloudEvent<AttributesImpl, String> cloudEvent = 
					CloudEventsHelper.parseFromRequest(headers, (Object) in.getPayload());
			String results = handleCloudEvent(cloudEvent);
			return MessageBuilder.withPayload(results).build();
		};
	}

	private String handleCloudEvent(CloudEvent<AttributesImpl, String> cloudEvent) {
		log.info("CLOUD EVENT ATTR: " + cloudEvent.getAttributes());
		log.info("CLOUD EVENT DATA: " + cloudEvent.getData());
		final String id = cloudEvent.getAttributes().getId();
		final String type = cloudEvent.getAttributes().getType();
		final String subject = cloudEvent.getAttributes().getSubject().orElse("");
		JsonNode node = null;
		try {
			node = mapper.readTree(cloudEvent.getData().get());
		} catch (Exception e) {
			log.info("No JSON Data: " + cloudEvent.getData());
		}
		if (node != null) {
			final JsonNode version = node.get("apiVersion");
			log.info("VERSION: " + version);
			final JsonNode involvedObject = node.get("involvedObject");
			if (involvedObject != null) {
				final JsonNode apiVersion = involvedObject.get("apiVersion");
				final JsonNode kind = involvedObject.get("kind");
				final JsonNode name = involvedObject.get("name");
				log.info("OBJECT: " + apiVersion + ":" + kind + "/" + name);
			}
			final JsonNode reason = node.get("reason");
			final JsonNode message = node.get("message");
			log.info("REASON: " + reason +":" + message);
		}
		log.info("MESSAGE: " + "Processed " + type + ":" + subject + ":" + id);
		return "Processed Event " + type + ":" + subject + ":" + id;
	}

	public static void main(final String[] args) {
		SpringApplication.run(K8sEventApplication.class, args);
	}

}
