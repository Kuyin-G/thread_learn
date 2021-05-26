package thread11.threal.pool.my;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPool {
    /**
     * 等待的任务队列
     * */
    private BlockingQueue<Runnable> taskQueue;

    /**
     * 线程集合
     * */
    private HashSet<Worker> workers = new HashSet<>();

    /**
     * 线程核心数
     * */
    private int coreSize;

    /**
     * 获取任务的超时时间
     * */
    private long timeout;

    /**
     * 时间单位
     * */
    private TimeUnit timeUnit;

    /**
     * 拒接策略
     * */
    private RejectPolicy<Runnable> rejectPolicy;

    /**
     * @param coreSize 线程核心数
     * @param timeout 获取任务的超时时间
     * @param timeUnit 时间单位
     * @param queueCapacity 等待任务队列的容量
     * */
    public ThreadPool(int coreSize,long timeout,TimeUnit timeUnit,int queueCapacity){
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
    }

    /**
     * @param coreSize 线程核心数
     * @param timeout 获取任务的超时时间
     * @param timeUnit 时间单位
     * @param queueCapacity 等待任务队列的容量
     * @param rejectPolicy 拒接策略
     * */
    public ThreadPool(int coreSize,long timeout,TimeUnit timeUnit,int queueCapacity,RejectPolicy<Runnable> rejectPolicy){
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    /**
     * 执行任务
     * */
    public void execute(Runnable task){
        synchronized (workers){
            if(workers.size() < coreSize){
                // 线程有空闲线程，立即用新线程去开启
                Worker worker = new Worker(task);
                log.info("新增工作线程:{},任务：{}",worker,task);
                workers.add(worker);
                worker.start();
            }else {
                if(rejectPolicy != null){
                    // 1)死等
                    // 2)带超时等待
                    // 3)让调用者自己调用
                    // 4)让调用者抛出异常
                    // 5）让调用者自己执行任务
                    log.info("加入到任务等待队列{}",task);
                    taskQueue.tryPut(rejectPolicy,task);
                }else{
                    // 所有线程都忙，存放到任务等待队列中
                    log.info("加入到任务等待队列{}",task);
                    taskQueue.put(task);
                }
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task){
            this.task = task;
        }

        @Override
        public void run() {
            while (task!=null || (task = taskQueue.take())!=null){
                try {
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    task = null;
                }
            }
            synchronized (workers){
                log.info("工作线程被移除：{}",this);
                workers.remove(this);
            }
        }
    }

}
