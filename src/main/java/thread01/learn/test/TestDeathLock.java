package thread01.learn.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TestDeathLock {

    /**
     * 死锁产生的条件： 彼此拥有对方的锁资源，同时需要对方的锁资源
     * */

    public Object lockA = new Object();
    public Object lockB = new Object();

    @Test
    public void testDeathLock() throws InterruptedException {
        Thread thread1 = new Thread(()->{
            log.info("正在尝试获得所资源lock_a");
            synchronized (lockA){
                log.info("获得所资源lock_a");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("正在尝试获得所资源lock_b");
                synchronized (lockB){
                    log.info("获得所资源lock_b");
                }
            }
        },"线程1：持有lock_a,等待lock_b释放锁");
        Thread thread2 = new Thread(()->{
            log.info("正在尝试获得所资源lock_b");
            synchronized (lockB){
                log.info("获得所资源lock_b");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("正在尝试获得所资源lock_a");
                synchronized (lockA){
                    log.info("获得所资源lock_a");
                }
            }
        },"线程2：持有lock_b,等待lock_a释放锁");

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

    public static ReentrantLock reentrantLockA = new ReentrantLock();
    public static ReentrantLock reentrantLockB = new ReentrantLock();


    /**
     * 外部打断通知，解除死锁
     * */
    @Test
    public void testDeadLock2() throws InterruptedException {
        Thread threadA = new Thread(()->{
            try {
                log.info("准备进入reentrantLockA");
                reentrantLockA.lockInterruptibly();
                log.info("获得reentrantLockA");
                Thread.sleep(1000);
                log.info("准备进入reentrantLockB");
                reentrantLockB.lockInterruptibly();
                log.info("进入reentrantLockB");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                if(reentrantLockB.isHeldByCurrentThread()){
                    reentrantLockB.unlock();
                }
                if(reentrantLockA.isHeldByCurrentThread()){
                    reentrantLockA.unlock();
                }
            }
        },"线程A");
        Thread threadB = new Thread(()->{
            try {
                log.info("准备进入reentrantLockB");
                reentrantLockB.lockInterruptibly();
                log.info("进入reentrantLockB");
                Thread.sleep(1000);
                log.info("准备进入reentrantLockA");
                reentrantLockA.lockInterruptibly();
                log.info("获得reentrantLockA");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                if(reentrantLockB.isHeldByCurrentThread()){
                    reentrantLockB.unlock();
                }
                if(reentrantLockA.isHeldByCurrentThread()){
                    reentrantLockA.unlock();
                }
            }
        },"线程B");

        threadA.start();
        threadB.start();
        // 10秒后中断其中一个线程B
        Thread.sleep(10000);
        threadB.interrupt();
        threadA.join();
        threadB.join();
    }

    /**
     * 锁申请等待限时
     * */
    @Test
    public void testTryLockWithLimitTime() throws InterruptedException {
        Thread threadA = new Thread(()->{
            try {
                log.info("准备进入reentrantLockA");
                reentrantLockA.tryLock(2, TimeUnit.SECONDS);
                log.info("获得reentrantLockA");
                Thread.sleep(1000);
                log.info("准备进入reentrantLockB");
                reentrantLockB.tryLock(2, TimeUnit.SECONDS);
                log.info("进入reentrantLockB");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                if(reentrantLockB.isHeldByCurrentThread()){
                    reentrantLockB.unlock();
                }
                if(reentrantLockA.isHeldByCurrentThread()){
                    reentrantLockA.unlock();
                }
            }
        },"线程A");
        Thread threadB = new Thread(()->{
            try {
                log.info("准备进入reentrantLockB");
                reentrantLockB.tryLock(2, TimeUnit.SECONDS);
                log.info("进入reentrantLockB");
                Thread.sleep(1000);
                log.info("准备进入reentrantLockA");
                reentrantLockA.tryLock(2, TimeUnit.SECONDS);
                log.info("获得reentrantLockA");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                if(reentrantLockB.isHeldByCurrentThread()){
                    reentrantLockB.unlock();
                }
                if(reentrantLockA.isHeldByCurrentThread()){
                    reentrantLockA.unlock();
                }
            }
        },"线程B");

        threadA.start();
        threadB.start();
        // 10秒后中断其中一个线程B
        Thread.sleep(10000);
    }


}
