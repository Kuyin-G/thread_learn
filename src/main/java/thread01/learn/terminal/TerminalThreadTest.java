package thread01.learn.terminal;

import org.junit.Test;

public class TerminalThreadTest {

    /**
     * 停止一个线程，不能直接使用Thread提供的方法，因为该方法会直接结束线程，同时也被JDK设置为过期方法，后续将会移除
     * */

    @Test
    public void testTerminalUseInterrupt() throws InterruptedException {

        Thread thread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    // 在线程停止前处理
                    doSomethingBeforeTerminal();
                    break;
                }
            }
            System.out.println("线程已经结束了");
        });

        thread.start();
        Thread.sleep(2000);
        thread.interrupt(); //打断线程，产生打断标记
        Thread.sleep(3000);
    }

    @Test
    public void testTerminalInterruptSleepThread() throws InterruptedException {

        Thread thread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    // 在线程停止前处理
                    doSomethingBeforeTerminal();
                    break;
                }

                try {
                    // sleep方法会清除打断标志：
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    current.interrupt(); // 重置打断标志，如果不重置打断标记无法退出
                    e.printStackTrace();
                }
            }
            System.out.println("线程已经结束了");
        });

        thread.start();
        Thread.sleep(2000);
        thread.interrupt(); //打断线程，产生打断标记
        Thread.sleep(3000);
    }

    public static void doSomethingBeforeTerminal(){
        System.out.println("在线程终止前，处理相关的事情!!!");
    }

}
