package thread01.learn.join;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试线程的join方法
 * */
public class InvokeJoinTest {

    @Test
    public void testJoin() throws InterruptedException {
        long start = System.currentTimeMillis();
        AtomicInteger i = new AtomicInteger();
        Thread thread = new Thread(() -> {
            try {


                // 休眠2秒针
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i.getAndIncrement();
        });

        thread.start();
        // 调用join，让主线程等thread线程的结束
        thread.join();
//        thread.join(1000);// 过期不候
//        System.out.println("过期不候");
        System.out.println(i.get());
        long spend = System.currentTimeMillis() - start;
        System.out.println("花费时间："+spend+"ms");
    }
}
