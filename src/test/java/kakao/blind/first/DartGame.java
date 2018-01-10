package kakao.blind.first;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DartGame {

	private static final Logger logger = LoggerFactory.getLogger(DartGame.class);

	public int getTotalScore(String input) {
		String regex = "[0-9]?[10]?[SDT][#*]?";

		Pattern numberFinder = Pattern.compile("[0-9]?[10]?");
		Pattern bonusFinder = Pattern.compile("[SDT]");
		Pattern optionFinder = Pattern.compile("[#*]");

		Matcher matcher = Pattern.compile(regex).matcher(input);

		List<GameResult> gameResults = new ArrayList<>();

		while(matcher.find()) {
			String gameResult = matcher.group();

			Matcher numberMatch = numberFinder.matcher(gameResult);
			numberMatch.find();
			int score = Integer.parseInt(numberMatch.group());


			Matcher bonusMatch = bonusFinder.matcher(gameResult);
			bonusMatch.find();
			String bonus = bonusMatch.group();

			String option = "-";
			Matcher optionMatch = optionFinder.matcher(gameResult);
			if (optionMatch.find()) {
				option = optionMatch.group();
			}

			gameResults.add(new GameResult(score, option, bonus));
		}

		for (int i =0; i < gameResults.size(); i++) {
			GameResult gameResult = gameResults.get(i);
			int score = gameResult.getScore() ;
			score = new Double(Math.pow(score, gameResult.pow())).intValue();

			if ("*".equals(gameResult.getOption())) {
				score = score * 2;

				if (i > 0) {
					gameResults.get(i-1).doubleResultScore();
				}
			}

			if ("#".equals(gameResult.getOption())) {
				score = -score;
			}

			gameResult.setResultScore(score);
		}

		int sum = gameResults.stream().mapToInt(GameResult::getResultScore).sum();

		gameResults.stream().forEach(each -> logger.info("{}", each));
		logger.info("Total score = {}", sum);

		return sum;
	}

	class GameResult {
		int score;
		String option;
		String bonus;
		int resultScore = 0;

		public GameResult(int score, String option, String bonus) {
			this.score = score;
			this.option = option;
			this.bonus = bonus;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public String getOption() {
			return option;
		}

		public void setOption(String option) {
			this.option = option;
		}

		public String getBonus() {
			return bonus;
		}

		public void setBonus(String bonus) {
			this.bonus = bonus;
		}

		public int getResultScore() {
			return resultScore;
		}

		public void setResultScore(int resultScore) {
			this.resultScore = resultScore;
		}

		public void doubleResultScore() {
			resultScore = resultScore * 2;
		}

		public int pow() {
			switch (bonus) {
				case "S" : return 1;
				case "D" : return 2;
				case "T" : return 3;
				default: throw new IllegalArgumentException("Wrong bonus. bonus=" + bonus);
			}
		}

		@Override
		public String toString() {
			return "GameResult{" +
					"score=" + score +
					", option=" + option +
					", bonus=" + bonus +
					", resultScore=" + resultScore +
					'}';
		}
	}

	@Test
	public void test2() {
		Assert.assertEquals(37, getTotalScore("1S2D*3T"));
		Assert.assertEquals(9, getTotalScore("1D2S#10S"));
		Assert.assertEquals(3, getTotalScore("1D2S0T"));
		Assert.assertEquals(23, getTotalScore("1S*2T*3S"));
		Assert.assertEquals(5, getTotalScore("1D#2S*3S"));
		Assert.assertEquals(-4, getTotalScore("1T2D3D#"));
		Assert.assertEquals(59, getTotalScore("1D2S3T*"));
	}
}
