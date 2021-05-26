package thread06.dead.lock;


import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.concurrent.locks.ReentrantLock;
/**
 * 筷子2.0版本，继承ReentrantLock
 * */
@ToString
@AllArgsConstructor
public class Chopstick_2 extends ReentrantLock {
    private String name;
}
