package interception;

import DIByRik.annotations.interception.Cacheable;

public class PrimeCalculator {
	@Cacheable
	public int amountOfPrimesUnder(int max) {
		int count = 1;  // 2 is prime

		// Not the most efficient way to do this, but it's a demo of @Cacheable
		// But at least skip the even numbers
		for (int i = 3; i < max; i += 2) {
			if (isPrime(i)) {
				count++;
			}
		}
		return count;
	}

	private boolean isPrime(int n) {
		for (int i = 2; i < n; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	@Cacheable
	public String getSomeString() {
		return "weee ".repeat(1000);
	}
}
