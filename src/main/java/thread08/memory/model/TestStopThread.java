package thread08.memory.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class TestStopThread {

    /**
     * 两阶段终止模式：
     *      在线程 t1如何在终止线程 t2 之前完成需要处理的事情（料理后事）？
     *
     *      错误思路：
     *          1、使用线程的stop()方法，如果这时线程锁住了共享资源，那么当他被stop方法杀死了，
     *          就再也没有机会释放锁，其他线程将永远无法获得锁资源
     *
     *          2、使用System.exit(int)方法停止线程：目的是停止一个线程，但这种做法会让这个程序都停止。
     *
     * */


    @Test
    public void testStopThread() throws InterruptedException {
        MonitorThread monitor = new MonitorThread();

        Thread.sleep(1000);
        monitor.start();

        Thread.sleep(10000);
        monitor.stop();
    }







}
