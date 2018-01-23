package kakao.blind.first;


import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class ShuttleBus {

	private static final Logger logger = LoggerFactory.getLogger(ShuttleBus.class);

	@Test
	public void test() {
		LocalTime lastBoardingTime = getLastBoardingTime(1, 1, 5, getTimeTable(Arrays.asList("08:00", "08:01", "08:02", "08:03")));
		Assert.assertEquals(LocalTime.of(9,0), lastBoardingTime);

		lastBoardingTime = getLastBoardingTime(2, 10, 2, getTimeTable(Arrays.asList("09:10", "09:09", "08:00")));
		Assert.assertEquals(LocalTime.of(9,9), lastBoardingTime);

		lastBoardingTime = getLastBoardingTime(2, 1, 2, getTimeTable(Arrays.asList("09:00", "09:00", "09:00", "09:00")));
		Assert.assertEquals(LocalTime.of(8,59), lastBoardingTime);

		lastBoardingTime = getLastBoardingTime(1, 1, 5, getTimeTable(Arrays.asList("00:01", "00:01", "00:01", "00:01", "00:01")));
		Assert.assertEquals(LocalTime.of(0,0), lastBoardingTime);

		lastBoardingTime = getLastBoardingTime(1, 1, 1, getTimeTable(Arrays.asList("23:59")));
		Assert.assertEquals(LocalTime.of(9,0), lastBoardingTime);

		lastBoardingTime = getLastBoardingTime(10, 60, 45, getTimeTable(Arrays.asList("23:59","23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59")));
		Assert.assertEquals(LocalTime.of(18,0), lastBoardingTime);

	}

	public List<LocalTime> getTimeTable(List<String> timeTable) {
		return timeTable.stream().map(each -> {
			String[] token = each.split(":");
			return LocalTime.of(Integer.parseInt(token[0]), Integer.parseInt(token[1]));
		}).collect(toList());
	}

	public LocalTime getLastBoardingTime(int n, int t, int m, List<LocalTime> passenger) {
		logger.info("######################## Start getLastBoardingTime ######################");

		List<LocalTime> busTime = getBusTime(n, t);
		Queue<LocalTime> waiting = queueingPassenger(passenger);

		LocalTime lastBoardingTime = waiting.peek().minusMinutes(1);


		for (int i = 0; i < busTime.size(); i++) {
			lastBoardingTime =  getLastBoardingTime(m, busTime.get(i), waiting);
		}

		logger.info("#################### Last Boarding Time = {} ######################", lastBoardingTime);
		return lastBoardingTime;
	}

	public LocalTime getLastBoardingTime(int m, LocalTime busTime, Queue<LocalTime> waiting) {
		LocalTime lastPassenger = busTime;
		while (m > 0 && !waiting.isEmpty() && !waiting.peek().isAfter(busTime)) {
			lastPassenger = waiting.poll();
			m--;
		}

		if (m == 0) {
			return lastPassenger.minusMinutes(1);
		} else {
			return busTime;
		}
	}

	public Queue<LocalTime> queueingPassenger(List<LocalTime> passengerTime) {
		return new PriorityQueue<>(passengerTime);
	}

	public List<LocalTime> getBusTime(int n, int t) {
		LocalTime busTime = LocalTime.of(9, 0);
		List<LocalTime> busTimeList = new ArrayList<>();
		busTimeList.add(busTime);

		for (int i = 0; i < n - 1; i++) {
			LocalTime nextBusTime = busTime.plusMinutes(t);
			busTimeList.add(nextBusTime);
			busTime = nextBusTime;
		}

		logger.info("busTime = {}", busTimeList);
		return busTimeList;
	}
}

