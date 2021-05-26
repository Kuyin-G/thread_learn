package thread10.compare.and.setting.atomic;

public class Person {
    volatile String name;

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
