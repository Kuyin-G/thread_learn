package thread12.juc;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author Kuyin
 * juc中CountDownLatch的测试
 */
@Slf4j
public class CountDownLatchTest {



    @Test
    public void testCountDownLatch() throws InterruptedException {

        final CountDownLatch countDown = new CountDownLatch(10);

        log.info("倒计时：开始");

        Executors.newSingleThreadExecutor().execute(() -> {
            while (countDown.getCount() > 0) {
                long count = countDown.getCount();
                log.info("倒计时: {}", count);
                countDown.countDown();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        countDown.await();
        log.info("倒计时：0");
        log.info("发射");
    }
}
