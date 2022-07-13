package thread01.learn.daemon;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DaemonThreadTest {

    /***
     * 创建守护守护线程的方法
     *      1、创建一个新的线程 thread
     *      2、在线程启动前调用Thread实例的 thread.setDaemon(true)，设置线程为当前线程的守护线程
     *      3、启动守护线程 -> thread.start();
     *      在所在线程结束的时候，守护线程也会停止
     * */

    @Test
    public void testDaemon() throws InterruptedException {
        
        /**
         * 1、创建一个新的线程 thread
         * */        
        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("我是守护线程，正在执行守护任务...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        /**
         * 2、在线程启动前调用Thread实例的 thread.setDaemon(true)，设置线程为当前线程的守护线程
         * */
        daemonThread.setDaemon(true); //设置为守护线程
        daemonThread.start();
        Thread.sleep(4000);
        /**
         * 这里的主线程程就是用户现程，thread是守护现程
         * 主线程结束后，thread守护线程也会结束
         * */
        System.out.println("4秒后结束主线程，thread守护线程也会结束");
    }

    /**
     * 在线程内部设置为守护线程: 
     * */
    @Test
    public void testDaemonInThread() throws InterruptedException {
        Thread daemonThread = new Thread(()->{
            log.info("线程开始运行");
            /**
             * 获取当前线程，判断守护线程还是用户线程
             * */
            log.info("当前线程是:{}",  Thread.currentThread().isDaemon()? "守护线程" : "用户线程");
            while (true){
                try {
                    log.info("{}:运行中....", Thread.currentThread().getName());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        daemonThread.setDaemon(true); 
        daemonThread.setName("守护线程");
        daemonThread.start();
        
        Thread.sleep(6000);
        log.info("用户线程{}结束", Thread.currentThread().getName());
    }
}
