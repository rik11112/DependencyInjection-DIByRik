package DIByRik;
public class DIByRikApplication {
    public static DependencyContainer run(Class<?> mainClass) {
        return new DependencyContainer(mainClass);
    }
}
