package thread01.learn.interrupt;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 测试线程interrupt相关的api
 * */
public class InvokeInterruptTest {

    /**
     * interrupt()：用于打断线程
     * isInterrupted：判断是否被打断，不会清除打断标记
     * Thread.interrupt()：判断是否中断，并清除中断标记
     * */
    @Test
    public void testInterrupt() throws InterruptedException {

        Thread thread = new Thread(()->{
            while (true){
                // 为了响应中断操作，必须在线程中判断线程是否被中断，再执行中断后的操作。
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
            }
            System.out.println("我被中断退出了");
        });

        thread.start();
        Thread.sleep(500);
        thread.interrupt();
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * 打断sleep的线程:
     *  在sleep期间打断会抛出java.lang.InterruptedException: sleep interrupted
     *      同时会清除打断信号，所以需要重置以下打断信号
     * */
    @Test
    public void testInterruptSleepThread() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("打断信号："+Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        boolean interrupted = thread.isInterrupted();
        System.out.println("打断标记是否存在:" + interrupted);
    }

    @Test
    public void testInterruptJoinThread() throws InterruptedException {

        Thread firstThread = new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        firstThread.start();
        Thread thread = new Thread(() -> {
            try {
                // 在有等待其他线程结束的时候，调用interrupt(),抛出java.lang.InterruptedException
                firstThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        boolean interrupted = thread.isInterrupted();
        System.out.println("打断标记是否存在:" + interrupted);
    }

    @Test
    public void testInterruptWaitThread() throws InterruptedException {
        Object lock = new Object();
        Thread thread = new Thread(() -> {
            synchronized(lock){
                try {
                    // 调用wait的方法必须是拥有锁对象,notify()也是一样
                    lock.wait(); // 在wait阶段被打断会抛出java.lang.InterruptedException
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
        boolean interrupted = thread.isInterrupted();
        System.out.println("打断标记是否存在:" + interrupted);

    }

}
