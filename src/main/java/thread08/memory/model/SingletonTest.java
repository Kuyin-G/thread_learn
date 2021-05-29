package thread08.memory.model;

import org.junit.Test;

public class SingletonTest {
    @Test
    public void testSingleton(){
        Singleton singleton = Singleton.getInstance();
        Singleton singleton2 = Singleton.getInstance();
        System.out.println(singleton == singleton2);
    }
}
