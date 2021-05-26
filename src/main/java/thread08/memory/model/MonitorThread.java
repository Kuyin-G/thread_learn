package thread08.memory.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonitorThread {

    private Thread monitor;
    private static volatile boolean start = false;
    private static volatile boolean stop = false;

    MonitorThread() {
    }

    /**
     * 初始化监控线程
     */
    private void initMonitorThread() {
        monitor = new Thread(() -> {
            Thread current = Thread.currentThread();
            while (true) {
                if (!start) {
                    doBeforeStart();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        current.interrupt();
                    }
                    continue;
                }

                // 是否需要停止线程
                if (stop) {
                    doBeforeStop();
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.info("执行监控任务");
                } catch (InterruptedException e) {
                    // 因为sleep出现以后后，会清除打断标记，需要重新设置打断标记
                    current.interrupt();
                }
            }
        }, "monitor监控线程");
        monitor.start();
    }

    /**
     * 在线程停止前，需要处理的任务
     */
    private void doBeforeStop() {
        log.info("料理后事");
    }

    /**
     * 停止监控线程
     */
    public void stop() {
        log.info("停止线程");
        stop = true;
        monitor.interrupt();
    }

    /**
     * 多次调用，只会启动一个监控线程
     * */
    public void start() {
        synchronized (this){
            if(start){
                return;
            }
            initMonitorThread();
            log.info("开始监控任务");
            start = true;
        }
    }

    private void doBeforeStart() {
        if(start){
            return;
        }
        log.info("监控线程已就绪，等待启动");
    }
}
