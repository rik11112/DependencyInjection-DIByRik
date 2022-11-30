package interception;

import DIByRik.DIByRikApplication;
import DIByRik.DependencyContainer;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class InterceptionTests {
	private static final Logger log = Logger.getLogger(DependencyContainer.class.getName());

	@Test
	void cachedMethodsShouldRunFasterTheSecondTimeWithSameArgsAndStillReturnTheRightResult() {
		var container = DIByRikApplication.run(InterceptionTests.class);
		var primeCalculator = container.getInstanceOfClass(PrimeCalculator.class);

		var start = System.currentTimeMillis();
		var amountOfPrimesUnder1000_1 = primeCalculator.amountOfPrimesUnder(10000);
		var end = System.currentTimeMillis();
		var time1 = end - start;

		start = System.currentTimeMillis();
		var amountOfPrimesUnder1000_2 = primeCalculator.amountOfPrimesUnder(10000);
		end = System.currentTimeMillis();
		var time2 = end - start;

		log.info(String.format("Time 1: %d, Time 2: %d, Difference: %d", time1, time2, time1 - time2));
		assertEquals(amountOfPrimesUnder1000_1, amountOfPrimesUnder1000_2);
		assertTrue(time2 < time1);
	}

	@Test
	void differentCachedMethodsShouldNotInterfereWithEachOther() {
		var container = DIByRikApplication.run(InterceptionTests.class);
		var primeCalculator = container.getInstanceOfClass(PrimeCalculator.class);

		var weeeExpected = "weee ".repeat(1000);
		var weeeActual = primeCalculator.getSomeString();

		var primeExpected = 168;
		var primeActual = primeCalculator.amountOfPrimesUnder(1000);

		assertEquals(weeeExpected, weeeActual);
		assertEquals(primeExpected, primeActual);
	}

	@Test
	void cachedMethodsShouldOnlyHitIfInputIsTheSame() {
		var container = DIByRikApplication.run(InterceptionTests.class);
		var primeCalculator = container.getInstanceOfClass(PrimeCalculator.class);

		assertEquals(95, primeCalculator.amountOfPrimesUnder(500));
		assertEquals(168, primeCalculator.amountOfPrimesUnder(1000));
	}
}
