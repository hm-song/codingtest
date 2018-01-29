package kakao.blind.first;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JakadeCalculator {

	private static final Logger logger = LoggerFactory.getLogger(JakadeCalculator.class);


	@Test
	public void test() {
		String input1 = "FRANCE";
		String input2 = "french";
		Assert.assertEquals(16384, getJakadeValue(input1, input2));

		input1 = "handshake";
		input2 = "shake hands";
		Assert.assertEquals(65536, getJakadeValue(input1, input2));

		input1 = "aa1+aa2";
		input2 = "AAAA12";
		Assert.assertEquals(43690, getJakadeValue(input1, input2));

		input1 = "E=M*C^2";
		input2 = "e=m*c^2";
		Assert.assertEquals(65536, getJakadeValue(input1, input2));
	}

	public int getJakadeValue(String input1, String input2) {
		if (input1.length() < 2 || input1.length() > 1000 || input2.length() < 2 || input2.length() > 1000) {
			throw new IllegalArgumentException("Input out of scope");
		}

		Map<String, Integer> group1 = splitStr(input1);
		Map<String, Integer> group2 = splitStr(input2);

		int sameValue = getSameValue(group1, group2);
		int sumValue = getSumValue(group1, group2);

		if (sameValue == 0) {
			return 1 * 65536;
		}


		BigDecimal result = BigDecimal.valueOf(sameValue)
				.multiply(BigDecimal.valueOf(65536))
				.divide(BigDecimal.valueOf(sumValue), 0, BigDecimal.ROUND_DOWN);

		logger.info("result = {}", result);
		
		return result.intValue();
	}

	private Map<String, Integer> splitStr(String input) {
		HashMap<String, Integer> group = new HashMap<>();
		for (int i = 0; i < (input.length() - 1); i++) {
			String token = input.substring(i, i + 2).toLowerCase();
			if (token.matches("[a-z][a-z]")) {
				if (group.get(token) == null) {
					group.put(token, 1);
				} else {
					group.put(token, group.get(token) + 1);
				}
			}
		}

		logger.info("{}", group);
		return group;
	}

	private int getSameValue(Map<String, Integer> group1, Map<String, Integer> group2) {
		HashMap<String, Integer> same = new HashMap<>();
		for (Map.Entry<String, Integer> each : group1.entrySet()) {
			if (same.get(each.getKey()) == null) {
				if (group2.get(each.getKey()) != null) {
					int max = Math.min(group1.get(each.getKey()), group2.get(each.getKey()));
					same.put(each.getKey(), max);
				}
			}
		}

		logger.info("same group = {}", same);

		int sum = 0;
		for (Map.Entry<String, Integer> each : same.entrySet()) {
			sum += each.getValue();
		}

		logger.info("same = {}", sum);
		return sum;
	}

	private int getSumValue(Map<String, Integer> group1, Map<String, Integer> group2) {
		HashMap<String, Integer> same = new HashMap<>();
		for (Map.Entry<String, Integer> each : group1.entrySet()) {
			if (same.get(each.getKey()) == null) {
				if (group2.get(each.getKey()) != null) {
					int max = Math.max(group1.get(each.getKey()), group2.get(each.getKey()));
					same.put(each.getKey(), max);
				} else {
					same.put(each.getKey(), group1.get(each.getKey()));
				}
			}
		}

		for (Map.Entry<String, Integer> each : group2.entrySet()) {
			if (same.get(each.getKey()) == null) {
				if (group1.get(each.getKey()) == null) {
					same.put(each.getKey(), group2.get(each.getKey()));
				}
			}
		}

		logger.info("sum group = {}", same);

		int sum = 0;
		for (Map.Entry<String, Integer> each : same.entrySet()) {
			sum += each.getValue();
		}

		logger.info("sum = {}", sum);
		return sum;
	}
}

