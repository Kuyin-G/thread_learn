package thread07.order.control;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class ThreadOrderTest {

    static Object lock = new Object();
    static boolean t1RunFlag = false;

    /**
     * 在线程t1之后运行线程t2
     */
    @Test
    public void testThreadOrder01() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            String name = Thread.currentThread().getName();
            synchronized (lock) {
                try {
                    Thread.sleep(5000);
                    log.info("{}已经运行", name);
                    t1RunFlag = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "线程1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                while (!t1RunFlag) {
                    log.info("等待线程t1运行结束");
                    try {
                        lock.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String name = Thread.currentThread().getName();
                log.info("{}正在运行", name);
            }
        }, "线程2");

        t1.start();
        t2.start();
        t2.join();
    }

    /**
     * 使用LockSupport进行运行的控制
     * park()
     * unpark(Thread t) :控制下一个运行的线程
     */
    @Test
    public void testThreadOrder02() throws InterruptedException {

        Thread t2 = new Thread(() -> {
            LockSupport.park();
            String name = Thread.currentThread().getName();
            log.info("{}正在运行", name);
        }, "线程t2");

        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String name = Thread.currentThread().getName();
            log.info("{}正在运行", name);
            LockSupport.unpark(t2);
        }, "线程t1");
        t1.start();
        t2.start();
        t2.join();
    }

    /**
     * 线程1输出 “a” ，线程2输出 “b” ，线程3输出 “c”，现在交替执行循环5次
     */
    @Test
    public void testThreadOrder03() {
        WaitNotify waitNotify = new WaitNotify(5, 1);
        new Thread(() -> {
            waitNotify.print("a", 1, 2);
        }).start();
        new Thread(() -> {
            waitNotify.print("b", 2, 3);
        }).start();
        new Thread(() -> {
            waitNotify.print("c", 3, 1);
        }).start();
    }

    @Test
    public void testThreadOrder04() throws InterruptedException {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(() -> {
            awaitSignal.print("a", a, b);
        }).start();
        new Thread(() -> {
            awaitSignal.print("b", b, c);
        }).start();
        Thread thread3 = new Thread(() -> {
            awaitSignal.print("c", c, a);
        });

        thread3.start();
        awaitSignal.lock();
        try {
            System.out.println("开始");
            a.signal();
        } finally {
            awaitSignal.unlock();
        }

        thread3.join();
    }

    static Thread a;
    static Thread b;
    static Thread c;

    @Test
    public void testThreadOrder05() {
        ParkUnpark parkUnpark = new ParkUnpark(5);

        a = new Thread(() -> {
            parkUnpark.print("a", b);
        });
        b = new Thread(() -> {
            parkUnpark.print("b", c);
        });
        c = new Thread(() -> {
            parkUnpark.print("c", a);
        });

        a.start();
        b.start();
        c.start();
        System.out.println("开始:");
        LockSupport.unpark(a);

    }

}
