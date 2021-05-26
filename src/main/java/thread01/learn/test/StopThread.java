package thread01.learn.test;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class StopThread {

    volatile boolean stop = false;

    @Test
    public void testStopThreadWithVolatile() throws InterruptedException {
        new Thread(()->{
            log.info("开始运行");
            while (true){
                if(stop){
                    log.info("正在停止方法");
                    break;
                }
            }
            log.info("结束线程");
        },"演示中断线程").start();
        // 2秒钟后停止线程
        Thread.sleep(2000);
        stop = true;
    }

    @Test
    public void testStopWithInterrupted() throws InterruptedException {
        Thread thread = new Thread(()->{
            log.info("开始运行");
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    log.info("正在停止方法");
                    break;
                }
            }
            log.info("结束线程");
            Thread.yield();
        },"演示中断线程");
        thread.start();
        // 2秒钟后停止线程
        Thread.sleep(2000);
        thread.interrupt();
    }

    /**
     * 如果线程的run方法中出现了sleep或者await
     * 就必须使用interrupt来进行打断
     * 如果在sleep期间，会抛出一个异常InterruptedException
     * */
    @Test
    public void interruptedWhereHasSleepOrAwait() throws InterruptedException {
        Thread thread = new Thread(()->{
            log.info("开始运行");
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    log.info("正在停止方法");
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.info("出现打断了");
                    Thread.currentThread().interrupt();
                }
            }
        },"测试在有sleep或者await的时候");
        thread.start();

        Thread.sleep(500);
        thread.interrupt();
    }

}
