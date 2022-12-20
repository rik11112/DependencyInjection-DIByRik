package demo.primecalculator;

import DIByRik.annotations.Component;
import DIByRik.annotations.interception.Cacheable;

@Component
public class PrimeCalculator {
    @Cacheable
    public int amountOfPrimesUnder(int max) {
        int count = 1;  // 2 is prime

        // Not the most efficient way to do this, but it's a demo of @Cacheable
        // But it at least skips the even numbers
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
                return true;
            }
        }
        return false;
    }
}
