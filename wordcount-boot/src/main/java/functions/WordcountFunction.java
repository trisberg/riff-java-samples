package functions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;

public class WordcountFunction implements Function<Flux<String>, Flux<Map<String, Integer>>> {

	@Autowired
	WindowProperties config;

	@Override
	public Flux<Map<String, Integer>> apply(final Flux<String> words) {
		return words.window(Duration.ofSeconds(config.getDuration()))
				.flatMap(w -> w.collect(HashMap::new, (map, word) -> map.merge(word, 1, Integer::sum)));
	}
}
