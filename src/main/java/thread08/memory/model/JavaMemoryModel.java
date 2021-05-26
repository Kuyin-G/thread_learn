package thread08.memory.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class JavaMemoryModel {

    /**
     * JMM即Java Memory Model，它定义了主存、工作内存抽象概念，底层对应着CPU寄存器、缓存、硬件内存、CPU指令
     *
     * JMM体现在以下几个方面
     *      - 原子性 ： 保证子陵不会受到线程上下文切换的影响
     *      - 可见性 ： 保证指令不会受到CPU缓存的影响
     *      - 有序性 ： 保证指令不会受到CPU指令并行优化的影响
     * */

    /**
     * 退不出的循环：
     *      main线程对run变量的修改对于t线程不可见，导致了thread线程无法停止
     * */
    static boolean run = true;

    @Test
    public void testThreadNoControlByRunFlag() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                if (!run){
                    break;
                }
            }

        });

        thread.start();

        Thread.sleep(1000);
        run = false;
        log.info("运行标志run完成");
        thread.join();
    }

    /**
     * 使用在synchronized代码块中，变量每次都会去主内存中获取，而不是工作内存,
     *      synchronized: 保证可见性和原子性
     * */
    @Test
    public  void testThreadControlByVoSynchronized() throws InterruptedException {
        Object lock = new Object();
        Thread thread = new Thread(() -> {
            while (true) {
                synchronized (lock){

                    if (!run){
                        break;
                    }

                }

            }
        });

        thread.start();

        Thread.sleep(1000);
        run = false;
        log.info("运行标志run完成");
        thread.join();
    }

    static volatile boolean runFlag  = true;
    @Test
    public void testThreadControlByVolatileRunFlag() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                if (!runFlag){
                    break;
                }
            }

        });

        thread.start();

        Thread.sleep(1000);
        runFlag = false;
        log.info("运行标志run完成");
        thread.join();

    }





}
