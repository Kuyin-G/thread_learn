package thread01.learn.start;

import org.junit.Test;

/**
 * 测试两次调用线程的start()方法
 * */
public class InvokeDoubleStartTest {

    @Test
    public void testInvokeDouble(){
        System.out.println("测试多次调用start()方法！！！");
        Thread thread = new Thread(()->{
            System.out.println("正在运行run方法");
        });

        thread.start();

        thread.start();
        // 多次调用抛出异常：java.lang.IllegalThreadStateException
    }
}
