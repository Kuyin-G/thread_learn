package thread01.learn.daemon;

import org.junit.Test;

public class TestDaemonThread {

    @Test
    public void testDaemon() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("我是守护线程，正在执行监控任务...");
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
