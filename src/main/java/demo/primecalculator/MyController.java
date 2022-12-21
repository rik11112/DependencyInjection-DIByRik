package demo.primecalculator;

import DIByRik.annotations.Controller;
import DIByRik.annotations.InputMapping;

@Controller
public class MyController {
    private final RandomClass randomClass;

    public MyController(RandomClass randomClass) {
        this.randomClass = randomClass;
    }

    @InputMapping(route = "doSomething")
    public String doSomething() {
         return randomClass.expensiveMethod("foo");
    }

    @InputMapping(route = "doSomethingElse")
    public String doSomethingElse() {
        return "I'm doing something else!";
    }
}
