package thread01.learn.create;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadCreator {

    /**
     * 一、测试使用继承或实现的方式
     *      1、继承Thread的方式： Thread 实现了 Runnable
     *      2、现Runnable接口
     *      3、Callable接口： FutureTask<V> 实现了 RunnableFuture<V> ->RunnableFuture<V>接口 继承了 Runnable
     *   实质上全都是实现了Runnable的run方法
     * */
    @Test
    public void testExtendsOrImpl() throws ExecutionException, InterruptedException {
        /**
         * 1、通过继承Thread，重写run方法
         * */
        ThreadExt threadExt = new ThreadExt();
        threadExt.start();

        /**
         * 2、通过实现Runnable接口，实现run方法，传递给Thread实例
         * */
        Runnable runnable = new RunnableImpl();
        Thread thread = new Thread(runnable);
        thread.start();

        /**
         * 2、通过实现Callable接口，实现call()方法，创建FutureTask实例，传递给Thread
         * - 带返回只得线程
         * */
        Callable<String> callable = new CallableImpl();
        FutureTask<String> futureTask = new FutureTask<>(callable);
        Thread threadTask = new Thread(futureTask);
        threadTask.start();
        String result = futureTask.get();
        System.out.println(result);
    }

    /**
     *   如果只需要使用一次，可以使用匿名内部类
     * */
    @Test
    public void testAnonymousClasses() throws ExecutionException, InterruptedException {

        Thread thread = new Thread(){
            @Override
            public void run(){
                System.out.println("使用匿名内部类创建一");
            }
        };
        thread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("使用匿名内部类创建二");
            }
        }).start();

        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "使用匿名内部类创三";
            }
        });
        new Thread(task).start();
        System.out.println(task.get());

    }

    /**
     * 进阶的可以使用拉姆表达式，这里只有接口可以使用，继承的方式不可以使用
     */
    @Test
    public void testLambda() throws ExecutionException, InterruptedException {

        new Thread(() -> System.out.println("使用匿名内部类创建二")).start();

        FutureTask<String> task = new FutureTask<>(() -> "使用匿名内部类创三");
        new Thread(task).start();
        System.out.println(task.get());
    }




}

/**
 * 创建线程的方式一：继承Thread类，重写run方法
 * */
class ThreadExt extends Thread{
    @Override
    public void run() {
        System.out.println("创建线程的方式一：继承Thread类，重写run方法");
    }
}


/**
 * 创建线程的方式二：实现Runnable接口，重写run方法
 * */
class RunnableImpl implements Runnable{

    @Override
    public void run() {
        System.out.println("创建线程的方式二：实现Runnable接口，重写run方法");
    }
}

/**
 * 创建线程的方式三：实现Callable接口，call方法
 * */
class CallableImpl implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("创建线程的方式三：实现Callable接口，call方法");
        return "call";
    }
}