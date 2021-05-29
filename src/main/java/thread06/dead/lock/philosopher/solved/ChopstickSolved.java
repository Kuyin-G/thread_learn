package thread06.dead.lock.philosopher.solved;


import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.concurrent.locks.ReentrantLock;
/**
 * 筷子2.0版本，继承ReentrantLock
 * */
@ToString
@AllArgsConstructor
public class ChopstickSolved extends ReentrantLock {
    private String name;
}
