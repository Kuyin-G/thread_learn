package thread01.learn.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class JoinAndYieldTest {

    @Test
    public void testJoin() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(()->{
            log.info("线程开始运行");
            try {
                log.info("线程休眠2秒钟");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"测试Join()的线程");

        thread.start();
        thread.join();
        long end = System.currentTimeMillis();
        log.info("共耗时{}毫秒",(end-start));
    }

    volatile  boolean yield = false;
    @Test
    public void testYield() throws InterruptedException {

        Thread thread = new Thread(()->{
            while (true){
                if(yield){
                    log.info("我让出的CPU资源");
                }
                try {
                    log.info("我得到了CPU资源,正在执行");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        },"测试Thread.yield()的线程");

        thread.start();
        Thread.sleep(1000);
        yield = true;
        log.info("设置yield为{},thread让出CPU资源",yield);
        // 让线程一致执行
        while (true){

        }
    }
}
