package thread01.learn.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TestVolatile {




    /**
     *
     * Volatile:
     *      1）保证可见性，
     *      2）不保证原子性
     *      3）防止指令重排
     *
     * */

    /**
     * 测试可见性
     * */
    public volatile int a = 0;
    @Test
    public  void testThreadVisible(){
        new Thread( ()->{
            log.info("在2秒后将设置a=2");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            a = 2;
        }).start();

        while (a==0){
            // a = 0 就一直运行
        }
        log.info("程序结束");
    }

    /**
     * 测试不保证原子性
     * */
    public  volatile  int b = 0;
    public AtomicInteger atomicInteger = new AtomicInteger(0);
    @Test
    public void testNoAtomic(){
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    b++;
                    atomicInteger.getAndIncrement();
                }
            }).start();
        }

        while (Thread.activeCount() > 2){
            Thread.yield();
        }
        log.info("b运行结果(不保证原子性)：{}",b);
        log.info("atomicInteger的值：{}",atomicInteger.get());
        log.info("线程结束");
    }


}
