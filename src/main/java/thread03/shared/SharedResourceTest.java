package thread03.shared;

import org.junit.Test;

public class SharedResourceTest {


    /**
     * 测试共享问题：多个线程修改同一个资源，导致共享资源出错的问题
     *      两个线程堆初始值为0的静态变量做一个自增的，一个自减，各自做10000次，结果是0吗？
     *      这里有点搞笑，测试了很多次都是：0
     *      下面的的结果可能是正数、负数、0.
     *      原因是在Java中对静态变量的自增，自减不是原子操作，要彻底理解必须从字节码的角度来理解
     *      i++的字节码：
     *          getstatic i // 获取静态变量的值
     *          iconst_1    // 准备常量1
     *          iadd        // 自增
     *          putstatic i // 将修改后的值存入静态变量i
     *
     *      i--的字节码：
     *          getstatic i // 获取静态变量的值
     *          iconst_1    // 准备常量1
     *          isub        // 自增
     *          putstatic i // 将修改后的值存入静态变量i
     *
     *     在Java的内存模型中，完成静态变量的自增、自减，需要在主存和工作内存中进行数据交换。
     *     在多线程交替运行的情况下，一旦出现在i++ 或 i-- 的指令中间出现了时间片用完，导致线程上下文切换，
     *          就会导致数据出错：
     *          例如：
     *              getstatic 0 // 获取静态变量的值
     *              iconst_1    // 准备常量1
     *              isub        // 自减， = -1
     *              ---------------------发生线程切换----------------------------
     *                                          getstatic 0 // 获取静态变量的值
     *                                          iconst_1    // 准备常量1
     *                                          iadd        // 自增 =1
     *                                          putstatic 1 // 将修改后的值1存入静态变量
     *               ---------------------发生线程切换----------------------------
     *              putstatic -1 // 将修改后的值-1存入静态变量
     *          原本预计是0的，但是会出现 -1的情况
     *
     *
     * */

    public static int counter = 0;
    @Test
    public void testSharedProblem() throws InterruptedException {
        Thread autoAddThread = new Thread(() -> {
            for (int i = 0; i < 10000000; i++) {
                counter++;
            }
        });

        Thread autoSubThread = new Thread(() -> {
            for (int i = 0; i < 10000000; i++) {
                counter--;
            }
        });

        autoAddThread.start();
        autoAddThread.join();

        autoSubThread.start();
        autoSubThread.join();

        System.out.println("计算结果：counter="+counter);
    }


    /**
     *  一段代码内如果存在对共享资源的多线程读写操作，称这段代码为临界区。
     *      使用synchronized解决问题
     *          这里来的synchronized加锁的对象必须时同一个，否则无法解决
     *
     * */

    final Object lockObject = new Object();
    @Test
    public void testSolutionWithSynchronized() throws InterruptedException {
        Thread autoAddThread = new Thread(() -> {
            for (int i = 0; i < 10000000; i++) {
                synchronized (lockObject){
                    counter++;
                }

            }
        });

        Thread autoSubThread = new Thread(() -> {
            for (int i = 0; i < 10000000; i++) {
                synchronized (lockObject){
                    counter--;
                }
            }
        });

        autoAddThread.start();
        autoAddThread.join();

        autoSubThread.start();
        autoSubThread.join();

        System.out.println("计算结果：counter="+counter);
    }

}
