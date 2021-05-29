package thread02.reentrant.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockTest {

    /**
     * ReentrantLock ：
     *      1、lock() :进行加锁，
     *      2、unlock():释放锁资源
     *      3、tryLock()、tryLock(long n, TimeUnit) :尝试进行加锁，返回加锁是否成功
     * */

    @Test
    public void testUseReentrantLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Thread thread = new Thread(() -> {
            log.info("尝试获得锁");
            lock.lock();
            try {
                log.info("获得锁");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.info("释放锁");
                lock.unlock();
            }
        });
        thread.start();
        thread.join();
    }
    /**
     * ReentrantLock的条件变量:
     *      1、支持多个条件变量，需要获得锁资源
     *      2、
     *
     *
     * */
    
    
    @Test
    public void testReentrantLockCondition(){
        
        ReentrantLock lock = new ReentrantLock();
        Condition waitPenCondition = lock.newCondition();
        Condition waitBookCondition = lock.newCondition();

    }


}
