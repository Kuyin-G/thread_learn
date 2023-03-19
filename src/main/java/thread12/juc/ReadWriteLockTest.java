package thread12.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class ReadWriteLockTest{

    public static ReentrantLock reentrantLock = new ReentrantLock();
    public static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public static Lock readLock = readWriteLock.readLock();
    public static Lock writeLock = readWriteLock.writeLock();
    private int value;



    @Test
    public void testOnlyUserReentrantLock(){
        ReadWriteLockTest readWriteLockTest = new ReadWriteLockTest();
        /**
         * */
        Runnable read = ()->{
            try {
                readWriteLockTest.handleRead(reentrantLock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable write = ()->{
            try {
                readWriteLockTest.handleWrite(reentrantLock, new Random().nextInt());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        

    }
    /**
     * 处理读请求
    * */
    public Object handleRead(Lock lock) throws InterruptedException {
        try {
            lock.lock();
            log.info("正在读取数据：{}", Thread.currentThread().getId());
            Thread.sleep(1000);
            return value;
        }finally {
            lock.unlock();
        }
    }

    public void handleWrite(Lock lock,int  index){
        try {
            lock.lock();
            log.info("正在写数据：{}", Thread.currentThread().getId());
            Thread.sleep(1000);
            value = index;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
