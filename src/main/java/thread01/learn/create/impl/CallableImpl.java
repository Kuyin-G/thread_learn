package thread01.learn.create.impl;

import java.util.concurrent.Callable;

/**
 * 创建线程的方式三：实现Callable接口，call方法
 * */
public class CallableImpl implements Callable<String> {

    @Override
    public String call() throws Exception {
        System.out.println("创建线程的方式三：实现Callable接口，call方法");
        return "call";
    }
}