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

		List<String> input = Arrays.asList("Jeju", "Pangyo", "Seoul", "NewYork", "LA");
		LRUCache lruCache = new LRUCache(0);
		for (String each : input) {
			lruCache.get(each);
		}
		logger.info("LRU Cache result = {}", lruCache.getPerformance());
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


	class Node {
		private Node pre;
		private Node next;
		private String data;

		public Node(String data) {
			this.data = data;
		}

		public Node(Node next, String data) {
			this.next = next;
			this.data = data;
		}

		public Node getPre() {
			return pre;
		}

		public void setPre(Node pre) {
			this.pre = pre;
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}

	class LRUCache {
		private int capacity;
		private Map<String, Node> map = new HashMap<>();
		private Node head;
		private Node tail;
		private int performance = 0;

		public LRUCache(int capacity) {
			this.capacity = capacity;
		}

		public String get(String input) {
			Node node = map.get(input);

			// 캐시 미스
			if (node == null) {
				performance += 5;
				node = new Node(input);

				setHead(node);
				map.put(input, node);

				// 초과할 경우 tail 삭제 후 tail 갱신
				if (map.size() > capacity) {
					map.remove(tail.getData());
					if (map.size() != 0) {
						tail.getPre().setNext(null);
						tail = tail.getPre();
					} else {
						tail = null;
						head = null;
					}
				}

			// 캐시 적중
			} else {
				performance += 1;
				setHead(node);
			}

			// re-ordering
			return node.getData();
		}

		protected void setHead(Node node) {
			if (head == null) {
				head = node;
				tail = node;
			} else {
				head.setPre(node);
				node.setNext(head);
				head = node;
			}
		}

		public int getPerformance() {
			return performance;
		}
	}
}
