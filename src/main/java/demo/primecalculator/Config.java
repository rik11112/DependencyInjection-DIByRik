package demo.primecalculator;

import DIByRik.annotations.Bean;
import DIByRik.annotations.Configuration;

@Configuration
public class Config {
    @Bean
    public KlasseDieGeBeantWord beanTest(String oneTwoThree, PrimeCalculator primeCalculator) {
        return new KlasseDieGeBeantWord(oneTwoThree + "-" + primeCalculator.amountOfPrimesUnder(100));
    }

    @Bean
    public String oneTwoThree() {
        return "123";
    }
}
