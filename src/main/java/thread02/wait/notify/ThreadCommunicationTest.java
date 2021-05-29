package thread02.wait.notify;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 测试线程之间通信
 * */
@Slf4j
public class ThreadCommunicationTest {
    static final Object waitingRoom = new Object();
    static boolean hasPen = false;
    static boolean hasBook = false;


    /**
     * 第一版：
     *      使用sleep进行通信：
     *          1）问题：在小明没有笔的时候，使用sleep的时候，占用了锁，
     *                  导致妈妈（线程）阻塞着，不能继续运行
     * */
    @Test
    public void testCommunicationWithSleep() throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.info("有笔吗？{}", (hasPen ? "有" : "没有"));

            synchronized (waitingRoom) {
                if (!hasPen) {
                    log.info("没有笔，不做做作业了，去玩了!!!");
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.info("有笔吗？{}", (hasPen ? "有" : "没有"));
                String name = Thread.currentThread().getName();
                if (hasPen) {
                    log.info("{},别玩了，去写作业", name);
                }
            }
        }, "小明");
        thread.start();
        Thread momThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (waitingRoom) {
                String name = Thread.currentThread().getName();
                log.info("{},要去煮饭了", name);
            }

        }, "妈妈");
        momThread.start();
        thread.join();
        momThread.join();
    }

    /**
     * 第二版：
     *      使用wait进行通信：
     *          1）问题：在小明没有笔的时候，使wait，让出了锁资源，
     *                  妈妈（线程）也能获得锁资源，继续运行
     *
     * */
    @Test
    public void testCommunicationWithWait() throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.info("有笔吗？{}", (hasPen ? "有" : "没有"));

            synchronized (waitingRoom) {
                if (!hasPen) {
                    log.info("没有笔，不做做作业了，去玩了!!!");
                    try {
                        waitingRoom.wait(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.info("有笔吗？{}", (hasPen ? "有" : "没有"));
                String name = Thread.currentThread().getName();
                if (hasPen) {
                    log.info("{},别玩了，快去写作业", name);
                }
            }
        }, "小明");
        thread.start();
        Thread momThread = new Thread(() -> {
            try {
                // 主要是等待thread运行后让出资源
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (waitingRoom) {
                String name = Thread.currentThread().getName();
                log.info("{},要去煮饭了", name);
            }

        }, "妈妈");
        momThread.start();
        thread.join();
        momThread.join();
    }

    /**
     * 第三版：
     *      使用wait进行通信：在多个线程中
     *          在小明没有笔的让一直等着，一旦有ben需要进行写作业
     *          这里不是if进行判断的原因是，当使用notify进行唤醒的时候，可能导致虚假唤醒，
     *          所以使用while就能在每次唤醒的出后判断是否具备条件，从而避免虚假唤醒
     *
     * */
    @Test
    public void testCommunicateWithWaitAndNotify() throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (waitingRoom){
                while (!hasPen){
                    log.info("没有笔就一直等着");
                    try {
                        waitingRoom.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.info("有笔吗？{}", (hasPen ? "有" : "没有"));
                String name = Thread.currentThread().getName();
                if(hasPen){
                    log.info("{},别玩了，快去写作业", name);
                }

            }
        }, "小明");
        thread.start();
        Thread.sleep(2000);
        log.info("2秒后，笔买了，通知小明去写作业");
        hasPen = true;
        synchronized (waitingRoom){
            waitingRoom.notify();
        }
    }


    /**
     * 第三版：
     *      使用wait进行通信：在多线程中测试
     *          在小明没有笔的让一直等着，一旦有ben需要进行写作业
     *
     * */
    @Test
    public void testCommunicateMoreThreadWithWaitAndNotify() throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (waitingRoom){
                String name = Thread.currentThread().getName();
                while (!hasPen){
                    log.info("{},没有笔就一直等着",name);
                    try {
                        waitingRoom.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.info("有笔吗？{}", (hasPen ? "有" : "没有"));
                if(hasPen){
                    log.info("{},别玩了，快去写作业", name);
                }

            }
        }, "小明");

        Thread thread2 = new Thread(() -> {
            synchronized (waitingRoom){
                String name = Thread.currentThread().getName();
                while (!hasBook){
                    log.info("{},没有笔记本就一直等着",name);
                    try {
                        waitingRoom.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                log.info("有作业本吗？{}", (hasBook ? "有" : "没有"));

                if(hasBook){
                    log.info("{},别玩了，快去写作业", name);
                }
            }
        }, "小红");
        thread.start();
        thread2.start();
        Thread.sleep(2000);
        log.info("2秒后，笔买了，通知小明去写作业");
        hasPen = true;
        hasBook = true;
        synchronized (waitingRoom){
            waitingRoom.notifyAll();
        }
        thread.join();
        thread2.join();
    }

}
