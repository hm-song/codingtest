package kakao.blind.first;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CacheQa {

	private static final Logger logger = LoggerFactory.getLogger(CacheQa.class);

	@Test
	public void test() {
		int score = getPerformance(3, Arrays.asList("Jeju", "Pangyo", "Seoul", "NewYork", "LA", "Jeju", "Pangyo", "Seoul", "NewYork", "LA"));
		Assert.assertEquals(50, score);
		logger.info("{}", score);

		score = getPerformance(3, Arrays.asList("Jeju", "Pangyo", "Seoul", "Jeju", "Pangyo", "Seoul", "Jeju", "Pangyo", "Seoul"));
		Assert.assertEquals(21, score);
		logger.info("{}", score);

		score = getPerformance(2, Arrays.asList("Jeju", "Pangyo", "Seoul", "NewYork", "LA", "SanFrancisco", "Seoul", "Rome", "Paris", "Jeju", "NewYork", "Rome"));
		Assert.assertEquals(60, score);
		logger.info("{}", score);

		score = getPerformance(5, Arrays.asList("Jeju", "Pangyo", "Seoul", "NewYork", "LA", "SanFrancisco", "Seoul", "Rome", "Paris", "Jeju", "NewYork", "Rome"));
		Assert.assertEquals(52, score);
		logger.info("{}", score);

		score = getPerformance(2, Arrays.asList("Jeju", "Pangyo", "NewYork", "newyork"));
		Assert.assertEquals(16, score);
		logger.info("{}", score);

		score = getPerformance(0, Arrays.asList("Jeju", "Pangyo", "Seoul", "NewYork", "LA"));
		Assert.assertEquals(25, score);
		logger.info("{}", score);
	}

	public int getPerformance(int cacheSize, List<String> input) {
		int hit = 1;
		int miss = 5;
		int score = 0;
		MapSearchEngine engine = new MapSearchEngine(cacheSize);

		for (String word : input) {
			int elapsed = engine.search(word) ? hit : miss;
			score += elapsed;
		}

		return score;
	}

	class MapSearchEngine {

		private int cacheSize = 0;

		private Map<String, Integer> cache = new HashMap<>();

		public MapSearchEngine(int cacheSize) {
			this.cacheSize = cacheSize;
		}

		public boolean search(String input) {
			input = input.toLowerCase();
			boolean hit = cache.get(input) != null;

			cache.entrySet().stream()
					.forEach(entry -> entry.setValue(entry.getValue() + 1));

			cache.put(input, 1);

			if (cache.size() > cacheSize) {
				Map<String, Integer> newone = new HashMap<>();
				cache.entrySet().stream()
						.sorted(Comparator.comparing(entry -> entry.getValue()))
						.limit(cacheSize)
						.forEach(entry -> newone.put(entry.getKey(), entry.getValue()));

				cache = newone;
			}

			return hit;
		}
	}
}
