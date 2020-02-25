package functions;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import io.cloudevents.CloudEvent;
import io.cloudevents.v03.AttributesImpl;
import io.cloudevents.v03.CloudEventBuilder;

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
			CloudEvent<AttributesImpl, JsonNode> cloudEvent = parseCloudEvent(headers, in.getPayload());
			String results = handleCloudEvent(cloudEvent);
			return MessageBuilder.withPayload(results).build();
		};
	}

	private String handleCloudEvent(CloudEvent<AttributesImpl, JsonNode> cloudEvent) {
		log.info("CLOUD EVENT ATTR: " + cloudEvent.getAttributes());
		log.info("CLOUD EVENT DATA: " + cloudEvent.getData());
		final String id = cloudEvent.getAttributes().getId();
		final String type = cloudEvent.getAttributes().getType();
		final String subject = cloudEvent.getAttributes().getSubject().orElse("");
		JsonNode node = cloudEvent.getData().orElse(null);
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
		} else {
			log.info("No JSON Data: " + cloudEvent.getData());
		}
		log.info("MESSAGE: " + "Processed " + type + ":" + subject + ":" + id);
		return "Processed Event " + type + ":" + subject + ":" + id;
	}

    private static CloudEvent<AttributesImpl, JsonNode> parseCloudEvent(Map<String, String> headers, JsonNode body) throws IllegalStateException {
        final String CE_ID = "ce-id";
		final String CE_TYPE = "ce-type";
		final String CE_SOURCE = "ce-source";
		final String CE_TIME = "ce-time";
		final String CE_DATACONTENTTYPE = "ce-datacontenttype";

	    if (headers.get(CE_ID) == null || (headers.get(CE_SOURCE) == null || headers.get(CE_TYPE) == null)) {
            throw new IllegalStateException("Cloud Event required fields are not present.");
        }

        return CloudEventBuilder.<JsonNode>builder().withId(headers.get(CE_ID))
                .withType(headers.get(CE_TYPE))
                .withSource((headers.get(CE_SOURCE) != null) ? URI.create(headers.get(CE_SOURCE)) : null)
                .withTime((headers.get(CE_TIME) != null) ? ZonedDateTime.parse(headers.get(CE_TIME)) : null)
                .withData((body != null) ? body : null)
                .withDatacontenttype((headers.get(CE_DATACONTENTTYPE) != null) ? headers.get(CE_DATACONTENTTYPE) : "application/json")
                .build();
    }

	public static void main(final String[] args) {
		SpringApplication.run(K8sEventApplication.class, args);
	}

}
