package kakao.blind.first;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TreasureMap {

	private static final Logger logger = LoggerFactory.getLogger(TreasureMap.class);

	@Test
	public void test() {
		int n = 5;
		Integer[] arr1 = {9, 20, 28, 18, 11};
		Integer[] arr2 = {30, 1, 21, 17, 28};
		String map = getMap(n, arr1, arr2);

		logger.info("{}", map);

		n = 6;
		Integer[] arr3 = {46, 33, 33 ,22, 31, 50};
		Integer[] arr4 = {27 ,56, 19, 14, 14, 10};
		map = getMap(n, arr3, arr4);

		logger.info("{}", map);
	}

	public String getMap(int n, Integer[] arr1, Integer[] arr2) {
		List<Integer[]> map1 = toMap(n, arr1);
		List<Integer[]> map2 = toMap(n, arr2);

		List<Integer[]> result = new ArrayList();

		for (int i = 0; i < n; i++) {
			Integer[] piece1 = map1.get(i);
			Integer[] piece2 = map2.get(i);
			Integer[] resultPiece = new Integer[n];
			for (int j = 0; j < n; j++) {
				resultPiece[j] = piece1[j] | piece2[j];
			}
			result.add(resultPiece);
		}

		List<String> map = result.stream()
				.map(arr ->
						Arrays.stream(arr)
								.map(value -> value == 1 ? "#" : " ")
								.reduce(String::concat)
								.get())
				.collect(toList());

		return map.stream().collect(Collectors.joining(",", "[", "]"));
	}

	public List<Integer[]> toMap(int n, Integer[] arr) {
		return Arrays.asList(arr).stream()
				.map(Integer::toBinaryString)
				.map(str -> {
					while (str.length() < n) {
						str = "0" + str;
					}
					return str;
				})
				.map(str -> Arrays.stream(str.split(""))
						.map(Integer::parseInt)
						.toArray(Integer[]::new))
				.collect(toList());
	}
}
