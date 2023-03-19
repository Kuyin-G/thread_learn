package thread12.juc.cas;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AtomicClassTest {
    
    
    /**
     * 单线程的调用API
     * */
    @Test
    public void testSingleThread(){
        AtomicInteger integer = new AtomicInteger(0);
        log.info("value: {}", integer.get());
        // 先取后
        log.info("getAndIncrement: {}",   integer.getAndIncrement());
        log.info("value:{}",integer.get());
        int expect = integer.get();
        integer.compareAndSet(expect,2);
        log.info("value: {}", integer.get());

        // i++
        integer.getAndIncrement();
        // ++i
        integer.incrementAndGet();
    }

    /**
     * CAS
     *它包含三个操作数- -一-内存位置、 预期原值及更新值。
     *  执行CAS操作的时候，将内存位置的值与预期原值比较:
     *  如果相匹配，那么处理器会自动将该位置值更新为新值，
     *  如果不匹配，处理器不做任何操作，多个线程同时执行CAS操作只有一个会成功。
     * */



    /**
     * 自旋锁的测试
     * */
    @Test
    public void testSpinLock() throws InterruptedException {
        SpinLock lock = new SpinLock();
        new Thread(()->{
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        },"线程1").start();

        TimeUnit.MICROSECONDS.sleep(100);
        new Thread(()->{
            lock.lock();

            lock.unlock();
        },"线程2").start();

        TimeUnit.SECONDS.sleep(10);
    }

}
