package thread12.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
public class CyclicBarrierTest {

    /**
     * CountDownLatch和CyclicBarrier的区别
     * 1、都是倒计时，但是CyclicBarrier是可以循环使用的的，一旦使用
     *
     * */


    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        int number = 8;
        CyclicBarrier barrier = new CyclicBarrier(number, ()->{
            log.info("人到齐了，开动了");
        });

        for (int i = 1; i <= number ; i++) {
            new Thread(()->{
                String name = Thread.currentThread().getName();
                log.info("{}到了",name);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                log.info("{}开始吃饭了");
            }, "第"+i+"个人").start();
        }

        testCyclicBarrier();
    }

    private static final Integer NUMBER = 7;


    /**
     * 测试集齐七龙珠的示例，七龙珠的集齐行为式可以重复的，所以使用cyclicBarrier，也就是
     * */
    public static void testCyclicBarrier() throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER,()->{
            log.info("集齐了7颗就可以召唤神龙了");
        });
        for (int j = 0; j < 5; j++) {

            for (int i = 1; i <= NUMBER; i++) {
                final int current = i;
                new Thread(()->{

                    try {
                        int nextInt = new Random().nextInt(10);
                        Thread.sleep(nextInt*1000);
                        log.info("第{}颗龙珠被被收集到了。",current);
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                },String.valueOf(i)).start();
            }
            // 线程进入休眠的原因是：只有一轮的龙珠收集齐了，召唤了神龙，才可以进行新一轮的收集
            Thread.sleep(11000);
        }
    }

}
