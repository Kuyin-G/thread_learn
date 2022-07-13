package thread01.learn.create.impl;
/**
 * 创建线程的方式二：实现Runnable接口，重写run方法
 * */
public class RunnableImpl implements Runnable{

    @Override
    public void run() {
        System.out.println("创建线程的方式二：实现Runnable接口，重写run方法");
    }
}