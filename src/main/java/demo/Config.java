package demo;

import DIByRik.annotations.Bean;
import DIByRik.annotations.Configuration;

@Configuration
public class Config {
    @Bean
    public KlasseDieGeBeantWord beanTest(String oneTwoThree, Weee weee) {
        return new KlasseDieGeBeantWord(oneTwoThree + "-" + weee.amountOfPrimesUnder(100));
    }

    @Bean
    public String oneTwoThree() {
        return "123";
    }
}
