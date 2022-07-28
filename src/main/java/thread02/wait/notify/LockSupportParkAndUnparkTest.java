package thread02.wait.notify;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class LockSupportParkAndUnparkTest {


    @Test
    public void testParkAndUnpark() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            log.info("线程： {}运行", Thread.currentThread().getName());
            LockSupport.park();
            log.info("线程：{}被唤醒", Thread.currentThread().getName());
            log.info("消耗时间： {}" , System.currentTimeMillis() - start);


        }, "T1");
        t1.start();

        TimeUnit.SECONDS.sleep(2);
        new Thread(()->{
            log.info("线程： {}发起通知", Thread.currentThread().getName());
            LockSupport.unpark(t1);
        }, "T2").start();
        TimeUnit.SECONDS.sleep(1);
    }
}
