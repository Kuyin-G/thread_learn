package thread01.learn.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockTest {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    public static int i = 0;

    /**
     * 可使用ReentrantLock锁替换synchronized关键字
     * */
    @Test
    public void testLock() throws InterruptedException {
        Thread thread = new Thread(()->{
            for (int j = 0; j<1000; j++){
                // 加锁
                lock.lock();
                try {
                    i++;
                }finally {
                    // 解锁
                    lock.unlock();
                }
            }
        },"测试ReentrantLock");

        thread.start();
        thread.join();
        log.info("i={}",i);
    }

    @Test
    public void testReentrantLockCondition() throws InterruptedException {
        Thread thread = new Thread(()->{
            try {
                lock.lock(); // 加锁
                log.info("加锁成功，进入等待");
                condition.await(); // 等待 ，必须再获得锁之后执行
                log.info("等待解除，继续执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); //解锁
            }
        },"测试ReentrantLock的条件Condition");
        thread.start();
        log.info("2秒后，唤醒条件等待的线程");
        Thread.sleep(2000);
        lock.lock();
        condition.signal();  // 必须再获得锁之后执行
        lock.unlock();
        Thread.sleep(1000);
    }

}

