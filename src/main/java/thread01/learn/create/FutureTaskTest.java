package thread01.learn.create;


import ch.qos.logback.core.util.TimeUtil;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.*;

@Slf4j
public class FutureTaskTest {
    
    @Test
    public void useFutureTask() throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(()->{
            Thread.sleep(10000);
            return "do something";
        });
        
        Thread taskThread = new Thread(futureTask);
        taskThread.start();
        long start = System.currentTimeMillis();
        log.info("运行结果：{}", futureTask.get());
        log.info("获取结果的时间: {}", System.currentTimeMillis()  - start);
        Thread.sleep(1000);
        log.info("运行结果：{}", futureTask.get());
    }
    
    @Test
    public void useFutureTaskAndThreadPool() throws InterruptedException, ExecutionException {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        FutureTask<String> task1 = new FutureTask<>(()->{
            Thread.sleep(2000);
            return "do something";
        });        
      
        FutureTask<String> task2 = new FutureTask<>(()->{
            Thread.sleep(2000);
            return "do something";
        });        
        FutureTask<String> task3 = new FutureTask<>(()->{
            Thread.sleep(2000);
            return "do something";
        });

        executorService.submit(task1);
        executorService.submit(task2);
        executorService.submit(task3);
        TimeUnit.MILLISECONDS.sleep(2000);
        /**
         * get()容易导致阻塞
         * */
        log.info("task1:{}", task1.get());
        log.info("task2:{}", task2.get());
        log.info("task3:{}", task3.get());
    }
    
    /**
     * 使用 public V get(long timeout, TimeUnit unit)
     *  timeout: 等待的时间，unit: 单位
     *  
     *  如果线程执行时间超过等待的时间，抛出异常java.util.concurrent.TimeoutException
     *  
     *  可以用，但是不优雅
     * */
    @Test
    public void testFutureTaskGetMethodWithParam() throws InterruptedException, ExecutionException, TimeoutException {

        FutureTask<String> task1 = new FutureTask<>(()->{
            Thread.sleep(2000);
            return "do something";
        });

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(task1);
        // 设置等待时间
        try {
            log.info("1秒后的运行结果: {}", task1.get(1, TimeUnit.SECONDS));
        }catch (Exception e){
            e.printStackTrace();
        }
        TimeUnit.SECONDS.sleep(2); // 睡眠2秒
        log.info("1秒后的运行结果: {}", task1.get(1, TimeUnit.SECONDS));
    }
    
    /**
     * 如果想要获取异步的结果可以使用轮循的方法去获取结果，尽量不要阻塞
     * 使用isDone()
     * 轮循的方法会耗费CPU的资源，
     * */
    @Test
    public void testFutureTaskIsDoneMethod() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<String> task = new FutureTask<>(()->{
            Thread.sleep(5000);
            return "finish";
        });

        executorService.submit(task);
        
        // 使用while循环进行循环执行
        while (true){
            if(task.isDone()){
                log.info("task的执行结果: {}", task.get());
                break;
            }else{
                // 为了避免日志打印过于频繁，进行睡眠
                TimeUnit.SECONDS.sleep(1);
                log.info("正在执行中，不要催了");
            }
        }
        log.info("执行完成，结束");
    }
}
