package thread01.learn.priority;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ThreadPriorityTest {

    /**
     * 线程的优先级：
     * */

    /**
     * 测试在10秒钟之内，每个
     * */
    @Test
    public void testPriority() throws InterruptedException {

        for (int i = Thread.MIN_PRIORITY; i <= Thread.MAX_PRIORITY ; i++) {
            Thread thread = new Thread(() -> {
                log.info("测试线程优先级::Priority");
               while (true){
                   try {
                       Thread.sleep(500);
                       log.info("线程{}::抢到的执行权！", Thread.currentThread().getName());
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            },String.valueOf(i));
            // 设置为最大优先级
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
        //
        Thread.sleep(10000);
    }

}
