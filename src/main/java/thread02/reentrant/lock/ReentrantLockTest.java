package thread02.reentrant.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockTest {

    /**
     * ReentrantLock ：
     *      1、lock() :进行加锁，
     *      2、unlock():释放锁资源
     *      3、tryLock()、tryLock(long n, TimeUnit) :尝试进行加锁，返回加锁是否成功
     *      4、lockInterruptibly(): 获得锁，但优先响应中断, 除非当前线程是 interrupted，否则获取锁定。
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
    public void testReentrantLockCondition() throws InterruptedException {
        
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(()->{
            lock.lock();
            log.info("线程：{} 进入", Thread.currentThread().getName());
            try {
                condition.await();
                log.info("线程：{}被唤醒", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }, "t1").start();

        TimeUnit.SECONDS.sleep(2);

        new Thread(()->{
            lock.lock();
            try {
                condition.signal();
                log.info("线程：{}发出通知", Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }

        }, "t2").start();

    }

    /**
     * 测试没有使用lock()/unlock的情况
     * */
    @Test
    public void testWithoutLockAndUnlock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(()->{
//            lock.lock();
            log.info("线程：{} 进入", Thread.currentThread().getName());
            try {
                condition.await();
                log.info("线程：{}被唤醒", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
//                lock.unlock();
            }

        }, "t1").start();

        TimeUnit.SECONDS.sleep(2);

        new Thread(()->{
//            lock.lock();
            try {
                condition.signal();
                log.info("线程：{}发出通知", Thread.currentThread().getName());
            } finally {
//                lock.unlock();
            }

        }, "t2").start();
    }

    /**
     * 解决死锁的两种方式
     * 1. 主动放弃： 在长时间获取不到所资源后，就进行进行主动放弃，释放自己获得的锁资源
     * 2. 有限等待：
     * */

    @Test
    public void testInterruptDeadThread() throws InterruptedException {
        InterruptDeadThread interruptDeadThread = new InterruptDeadThread(1);
        InterruptDeadThread interruptDeadThread2 = new InterruptDeadThread(2);

        Thread thread1 = new Thread(interruptDeadThread, "线程1");
        Thread thread2 = new Thread(interruptDeadThread2, "线程2");
        thread1.start();
        thread2.start();

        Thread.sleep(1000);
        thread2.interrupt();
        Thread.sleep(1000);
    }

}

