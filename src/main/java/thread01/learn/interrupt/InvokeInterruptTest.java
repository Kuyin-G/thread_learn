package thread01.learn.interrupt;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 测试线程interrupt相关的api
 * */
@Slf4j
public class InvokeInterruptTest {

    /**
     * 方式1：
     *      使用volatile字段修饰一个公共属性，
     */
    public volatile boolean isInterrupted=  false;
    
    @Test
    public void testUseVolatileInterrupt() throws InterruptedException {
        Thread thread = new Thread(()->{
            while (true){
                if(isInterrupted){
                    log.info("{}已被中断",Thread.currentThread().getName());
                    break; // 跳出循环，即可结束线程的运行，
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("正在运行的线程：{}", Thread.currentThread().getName());
            }
        },"测试volatile变量中断线程");
        
        thread.start();
        // 2秒后修改中断标志
        TimeUnit.SECONDS.sleep(2);
        log.info("修改中断标志为true");
        isInterrupted = true;
        TimeUnit.SECONDS.sleep(2);
        
    }
    
    /**
     * 使用AtomicBoolean进行中断线程：
     *      
     * */
    public AtomicBoolean atomicInterrupted = new AtomicBoolean(false);
    
    @Test
    public void testUseAtomicBooleanInterrupt() throws InterruptedException {
        Thread thread = new Thread(()->{
            while (true){
                if(atomicInterrupted.get()){
                    log.info("{}已被中断",Thread.currentThread().getName());
                    break; // 跳出循环，即可结束线程的运行，
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("正在运行的线程：{}", Thread.currentThread().getName());
            }
        },"测试volatile变量中断线程");

        thread.start();
        // 2秒后修改中断标志
        TimeUnit.SECONDS.sleep(2);
        log.info("修改中断标志为true");
        atomicInterrupted.compareAndSet(false,true);
        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * 方式三:
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
