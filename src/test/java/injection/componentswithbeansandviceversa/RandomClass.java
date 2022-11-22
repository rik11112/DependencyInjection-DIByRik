package injection.componentswithbeansandviceversa;

import DIByRik.annotations.Component;
import DIByRik.annotations.Service;

@Service
public interface RandomClass {
    String randomMethod();
}
