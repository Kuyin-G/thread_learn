package thread11.threal.pool.my;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MyThreadPoolTest {


    /**
     * 测试没有拒绝策列的线程池
     * */
    @Test
    public void testMyThreadPool(){
        ThreadPool pool = new ThreadPool(2,1000, TimeUnit.MILLISECONDS,10);
        for (int i = 0; i < 5; i++) {
            int j = i;
            pool.execute(()->{
                log.info("j:{}",j);
            });
        }
    }


    @Test
    public void testMyThreadPoolWithRejectPolicy(){

        /* 让主线程自己运行 */
        RejectPolicy<Runnable> rejectPolicy = (queue,task)->{
            task.run();
        };
        ThreadPool pool = new ThreadPool(2,
                1000,
                TimeUnit.MILLISECONDS,
                3,
                rejectPolicy);
        for (int i = 0; i < 10; i++) {
            int j = i;
            pool.execute(()->{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("j:{}",j);
            });
        }
        while (true){}
    }

}
