package thread01.learn.future;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 尽量不要使用new关键字来使用CompletableFuture而是使用：
 *  静态方法runAsync、supplyAsync来获取CompletableFuture
 * */
@Slf4j
public class CompletableFutureTest {
    
    
    /**
     * 没有返回值的runAsync：
     *  public static CompletableFuture<Void> runAsync(Runnable runnable) 
     * 
     * 可以传递线程池：
     * CompletableFuture<Void> runAsync(Runnable runnable,Executor executor)
     * */
    @Test
    public void testRunAsyncMethod() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            CompletableFuture.runAsync(()->{
                log.info("线程:{}线程开始",Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("线程:{}线程结束",Thread.currentThread().getName());
            },executorService);
        }
        
        TimeUnit.SECONDS.sleep(10);
    }
    
    
    /**
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
     * 
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier,Executor executor)
     * */
    @Test
    public void testSupplyAsyncMethod() throws ExecutionException, InterruptedException {
        log.info("测试CompletableFuture的supplyAsync()方法");
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("线程:{}线程开始", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("线程:{}线程结束", Thread.currentThread().getName());
            return "finish";
        }, Executors.newFixedThreadPool(3));
        String s = completableFuture.get();
        log.info("运行结果: {}", s);

    }


    /**
     * CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action)
     * 
     * 
     * public CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn)
     * */
    @Test
    public void testWhenCompleteAndExceptionally() throws ExecutionException, InterruptedException {
        log.info("测试CompletableFuture的supplyAsync()方法");
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("线程:{}线程开始", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("线程:{}线程结束", Thread.currentThread().getName());
            int intResult = ThreadLocalRandom.current().nextInt(10);
            if(intResult > 5){
                // 对于结果大于5的抛出异常
                throw new IllegalArgumentException("返回结果大于5");
            }
            return String.valueOf(intResult);
        }, Executors.newFixedThreadPool(3))
                .whenComplete((v,e)->{
                    // 线程完成了
                    if (e == null){
                        log.info("已经结果了，快来看看");
                    }
                }).exceptionally(e->{
                    // 出现异常情况
                    log.info("出异常了");
                    e.printStackTrace();
                    return null;
                });
        String s = completableFuture.get();
        log.info("运行结果: {}", s);

    }
    
    @Test
    public void testChaAt(){
        String mysql = "mysql";
        System.out.println(0.0d + mysql.charAt(0));
    }
    
    
    /**
     * 模拟各大平台
     * */
    static List<NetMall> netMallList = Arrays.asList(
            new NetMall("jd"),
            new NetMall("pdd"),
            new NetMall("tb")
    );
    
    @Test
    public void testGetPriceByCompletableFuture(){
        long start = System.currentTimeMillis();
        List<String> priceList = NetMall.getPrice(netMallList, "MySQL");
        priceList.forEach(log::info);
        log.info("耗费时间：{}", System.currentTimeMillis() -start);
        start = System.currentTimeMillis();

        List<String> mySQL2 = NetMall.getPriceByCompletableFuture(netMallList, "MySQL2");
        mySQL2.forEach(log::info);
        log.info("耗费时间：{}", System.currentTimeMillis() -start);
    }
    
    
    /**
     * 按步骤进行处理
     * thenApply：
     *  public <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn) 
     * */
    @Test
    public void testThenApplyMethod() throws InterruptedException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);

        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("第一步");
            log.info("当前线程：{}", Thread.currentThread().getName());
            return 1;
        }).thenApply((x) -> {
            log.info("上一步的计算结果: {}", x);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("第二步");
            log.info("当前线程：{}", Thread.currentThread().getName());
            return 2;
        }).thenApply(x -> {
            log.info("上一步的计算结果: {}", x);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("第三步");
            log.info("当前线程：{}", Thread.currentThread().getName());
            return 3;
        }).whenComplete((x,e)->{
            log.info("计算结果：{}",x);
        }).exceptionally(e->{
            e.printStackTrace();
            return null;
        });

        TimeUnit.SECONDS.sleep(5);
    }
    
    /**
     * 
     * public CompletableFuture<Void> thenAccept(Consumer<? super T> action) 
     * */
    @Test
    public void testThenAcceptMethod() throws InterruptedException {
        CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).thenAccept(result->{
            // 消费型的，没有返回值
            log.info("结果：{}", result);
        });
        
        TimeUnit.SECONDS.sleep(5);
    }
    
    /**
     * 多个线程同时运行，返回运行比较快的线程
     * public <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn)
     * */
    @Test
    public void testApplyToEither() throws InterruptedException {
        CompletableFuture<String> a = CompletableFuture.supplyAsync(() -> {
            log.info("A come in");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "A";
        });


        CompletableFuture<String> b = CompletableFuture.supplyAsync(() -> {
            log.info("B come in");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "B";
        });

        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> {
            log.info("C come in");
            try {
                TimeUnit.MILLISECONDS.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "C";
        });

        Function<String,String> function = param -> param + " is Win";

        CompletableFuture<String> result = a.applyToEither(b, function).applyToEither(c, function);
        log.info("比赛结果：{}", result.join());
        TimeUnit.SECONDS.sleep(4);
    }
    
    
    /**
     * 多个线程同时开始，在结束后，统计结果
     * */
    @Test
    public void testThenCombineMethod() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ThreadLocalRandom.current().nextInt(100);
        });
        CompletableFuture<Integer> sumFuture = future;
        for (int i = 0; i < 10; i++) {
            sumFuture = sumFuture.thenCombine(CompletableFuture.supplyAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(10) * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ThreadLocalRandom.current().nextInt(100);
            }), Integer::sum);
        }
        
        log.info("最终计算结果：{}", sumFuture.get());
        log.info("耗时：{}", System.currentTimeMillis() - start);
        TimeUnit.SECONDS.sleep(4);
    }
}


@Data
@AllArgsConstructor
class NetMall{
    private String netMallName;
    
    public  double calPrice(String productName){
        try {
            // 这里模拟从网上查找产品的价格
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ThreadLocalRandom.current().nextDouble() * 2  + productName.charAt(0);
    }
    
    public static List<String> getPrice(List<NetMall> netMallList, String productName){
        return netMallList
                .stream()
                    .map(mall -> String.format(productName + "in %s price is %f",
                        mall.getNetMallName(),
                        mall.calPrice(productName))
                    )
                .collect(Collectors.toList());
    }
    
    public static List<String> getPriceByCompletableFuture(List<NetMall> netMalls, String productName){
        return netMalls.stream()
                .map(netMall -> CompletableFuture.supplyAsync(()->
                    String.format(productName + "in %s price is %f",
                            netMall.getNetMallName(),netMall.calPrice(productName))
                ))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}