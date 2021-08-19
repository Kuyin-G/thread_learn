package thread01.learn.pool;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;


@Slf4j
public class ThreadPoolTest {

    /**
     * 测试固定的线程池
     * */
    @Test
    public void testFixedThreadPool() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i = 1; i<= 40; i++){
            int temp = i;
            executorService.submit(()->{
                try {
                    Thread.sleep(1000);
                    log.info("temp:{}",temp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        // 让主线程等待10秒
        Thread.sleep(10000);
    }

    /**
     * 测试计划任务
     * */
    @Test
    public void  testScheduledThread() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        for(int i = 0; i<5 ;i++){
            scheduledExecutorService.schedule(()->{
                log.info("定时执行");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },1, TimeUnit.SECONDS);
        }

        Thread.sleep(20000);
    }
}
