package thread01.learn.create.ext;

/**
 * 创建线程的方式一：继承Thread类，重写run方法
 * */
public class ThreadExt extends Thread{
    @Override
    public void run() {
        System.out.println("创建线程的方式一：继承Thread类，重写run方法");
    }
}