package thread02.wait.notify;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ObjectWaitAndNotifyTest {

    /**
     *
     * Object类提供的方法：
     *      wait: Object类的wait()、wait(long n)让进入object监视器的线程到waitSet等待。
     *      notify: Object类的notify在object上在waitSet等待的线程中挑一个唤醒。
     *      notifyAll: Object类的notifyAll()让object正在waitSet上等待的线程全部唤醒。
     *
     * 上面的调用Object的wait、notify、notifyAll方法，必须在获得锁资源，否则会被报错
     *      java.lang.IllegalMonitorStateException
     * */


    /**
     * 测试没有object没有获得锁资源的时候，调用wait方法
     * */
    @Test
     public void testWaitWithoutSynchronized() throws InterruptedException {
        Object object = new Object();
        object.wait();
     }


    /**
     * 单个线程的时候
     * */
    @Test
    public void testWait() throws InterruptedException {
        Object object = new Object();

        new Thread(()->{
            synchronized (object){
                log.info("调用wait方法，线程进入WAITING状态");

                try {
                    // 会让当前线程让出cpu的执行全
                    object.wait ();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                log.info("其他线程调用notify方法，线程被唤醒！");
            }
        }).start();

        System.out.println("2秒后唤醒正在WAITING的线程");
        Thread.sleep(2000);
        synchronized (object){
            object.notify();
        }
    }

    /**
     * 多个线程的时候，
     * */
    @Test
    public void testMoreThread() throws InterruptedException {
        Object object = new Object();
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                synchronized (object){
                    log.info("调用wait方法，线程进入WAITING状态");

                    try {
                        // 会让当前线程让出cpu的执行全
                        object.wait ();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String name = Thread.currentThread().getName();
                    log.info("{}:其他线程调用notify方法，线程被唤醒！",name);
                }
            },"Wait线程"+i).start();
        }

        log.info("2秒后唤醒正在WAITING的一个线程");
        Thread.sleep(2000);
        synchronized (object){
            object.notify();
        }
        log.info("2秒后唤醒正在WAITING的所有线程");
        Thread.sleep(2000);
        synchronized (object){
            object.notifyAll();
        }
    }

}
