package demo.primecalculator;

import DIByRik.annotations.*;
import DIByRik.annotations.interception.Timed;

@Repository
public class RandomClassImpl implements RandomClass {

	private PrimeCalculator primeCalculator;
	private KlasseDieGeBeantWord klasseDieGeBeantWord;

	public RandomClassImpl() {
	}

	@ConstructorInjection
	public RandomClassImpl(PrimeCalculator primeCalculator, KlasseDieGeBeantWord klasseDieGeBeantWord) {
		this.primeCalculator = primeCalculator;
		this.klasseDieGeBeantWord = klasseDieGeBeantWord;
	}

	@Timed
	@Override
	public String expensiveMethod(String foo) {
		return "I'm a random method!" + primeCalculator.amountOfPrimesUnder(100) + " <" + foo + "> " + klasseDieGeBeantWord.getTest();
	}
}
