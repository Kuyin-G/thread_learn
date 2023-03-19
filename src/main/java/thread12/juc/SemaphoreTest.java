package thread12.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
/**
 * 计数信号量。 从概念上讲，信号量保持一组许可。
 * */
public class SemaphoreTest implements Runnable {

    /**
     * Semaphore(int permits)
     * 使用给定数量的许可和非公平公平设置创建 Semaphore 。
     * Semaphore(int permits, boolean fair)
     * 使用给定数量的许可和给定的公平设置创建 Semaphore 。
     *
     * */
    final Semaphore semaphore = new Semaphore(2);


    @Test
    public void test() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        SemaphoreTest semaphoreTest = new SemaphoreTest();
        for (int i = 0; i <20; i++) {
            executorService.submit(semaphoreTest);
        }
    }

    @Override
    public void run() {
        try {
            // 获取
            semaphore.acquire();
            log.info("当前线{}程获得", Thread.currentThread().getId());
            // 模拟耗时任务
            Thread.sleep(5000);
            // 释放
            log.info("当前线{}程释放", Thread.currentThread().getId());
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
