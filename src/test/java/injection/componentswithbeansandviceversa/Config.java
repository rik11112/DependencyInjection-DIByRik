package injection.componentswithbeansandviceversa;

import DIByRik.annotations.Bean;
import DIByRik.annotations.Configuration;

@Configuration
public class Config {
    @Bean
    public BeanyClass beanTest(String oneTwoThree, Weee weee) {
        return new BeanyClass(oneTwoThree + "-" + weee.weee());
    }

    @Bean
    public String oneTwoThree() {
        return "123";
    }
}
