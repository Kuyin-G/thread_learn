package thread01.learn.daemon;

import org.junit.Test;

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
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("我是守护线程，正在执行守护任务...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.setDaemon(true); //设置为守护线程
        thread.start();
        Thread.sleep(4000);
        System.out.println("4秒后结束主线程，thread守护线程也会结束");
    }
}
