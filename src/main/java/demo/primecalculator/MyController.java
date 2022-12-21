package demo.primecalculator;

import DIByRik.annotations.Controller;
import DIByRik.annotations.InputMapping;

@Controller
public class MyController {
    private final RandomClass randomClass;

    public MyController(RandomClass randomClass) {
        this.randomClass = randomClass;
    }

    @InputMapping(input = "doSomething")
    public String doSomething() {
         return randomClass.expensiveMethod("foo");
    }

    @InputMapping(input = "doSomethingElse")
    public void doSomethingElse() {
        System.out.println("I'm doing something else!");
    }
}
