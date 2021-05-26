package thread11.threal.pool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class TestJDKThreadPool {


    /**
     * newFixedThreadPool(int coreThreadSize) ：
     * 创建固定核心线程的，等待阻塞队列是无界的
     */
    @Test
    public void testNewFixedThreadPool01() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 1; i <= 10; i++) {
            int value = i;
            executorService.execute(() -> {
                log.info("value: {}", value);
            });
        }
        Thread.sleep(2000);
    }


    /**
     * newFixedThreadPool(int nThreads, ThreadFactory threadFactory) ：
     * nThreads：创建固定核心线程的，等待阻塞队列是无界的
     * ThreadFactory：创建线程的方法
     */
    @Test
    public void testNewFixedThreadPool02() throws InterruptedException {

        // 创建线程的工厂
        ThreadFactory threadFactory = task -> {
            log.info("创建线程");
            return new Thread(task);
        };
        ExecutorService executorService = Executors.newFixedThreadPool(2, threadFactory);
        for (int i = 1; i <= 10; i++) {
            int value = i;
            executorService.execute(() -> {
                log.info("value: {}", value);
            });
        }
        Thread.sleep(2000);
    }


    /**
     * ExecutorServiced的方法：
     * execute(Runnable run): 创建新的的线程任务，没有返回值
     * submit(Callable<?> call) : 创建线程，具有返回值
     */
    @Test
    public void testExecuteAndSubmit() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.execute(() -> {
            log.info("execute");
        });

        Future<?> submit01 = executorService.submit(() -> {
            log.info("submit");
        });
        Object objResult = submit01.get();
        log.info("submit: {}", objResult);

        Future<String> submit02 = executorService.submit(() -> {
            String value = "result";
            log.info("value: {}", value);
            return value;
        });

        String result = submit02.get();
        log.info("result: {}", result);
    }

    /**
     * NewCachedThreadPool:
     * 1、核心线程是0，最大线程数是Integer.MAX_VALUE,救急线程的空闲生存时间是60s，意味着
     * - 全部线程是救急线程（60s以后可以回收）
     * - 救急线程理论上可以无限创建
     * 2、队列采用了SynchronousQueue实现特点是，它没有容量，没有线程来取是放不进去的
     * 3、整个线程池标为会根据任务量不断增长，没有上限，当任务执行完毕，空闲1分钟后释放线程，
     * -使用任务数比较密集，但每个任务执行时间较短的情况。
     */
    static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    public void testNewCachedThreadPool() throws InterruptedException {

        ThreadFactory factory = createThreadFactory();

        ExecutorService executorService = Executors.newCachedThreadPool(factory);
        for (int i = 0; i < 100; i++) {
            log.info("当前线程数：{}", atomicInteger.get());
            int value = i;
            executorService.execute(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("value: {}", value);

            });
        }
        Thread.sleep(2000);
        int threadSize = atomicInteger.get();
        log.info("threadSize: {}", threadSize);
    }

    /**
     * newSingleThreadExecutor
     * 1、希望多个任务排队执行，线程数固定为 1，任务数大于1是，存放到无界队列中排队。
     * - 任务执行完毕，这一个线程也不会被释放
     * 2、区别
     * -自己创建一个单线程执行任务，如果任务执行失败而终止，那么线程没有任何补救措施，
     * 而线程还会创建一个线程，保证线程池的正常工作。
     * -线程数始终未1，不过修改。
     * */
    @Test
    public void testNewSingleThreadExecutor() throws InterruptedException {
        ThreadFactory factory = createThreadFactory();
        ExecutorService executorService = Executors.newSingleThreadExecutor(factory);
        for (int i = 0; i < 10; i++) {
            log.info("当前线程数：{}", atomicInteger.get());
            int value = i;
            executorService.execute(()->{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("value: {}",value);
            });
        }
        Thread.sleep(2000);
        int threadSize = atomicInteger.get();
        log.info("threadSize: {}", threadSize);
    }

    public ThreadFactory createThreadFactory(){
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable run) {
                synchronized (this) {
                    while (true) {
                        int expect = atomicInteger.get();
                        int update = expect + 1;
                        boolean success = atomicInteger.compareAndSet(expect, update);
                        if (success) {
                            break;
                        }
                    }
                }
                return new Thread(run);
            }
        };
        return factory;
    }
}
