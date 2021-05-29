package thread01.learn.priority;

import org.junit.Test;

public class ThreadPriorityTest {

    @Test
    public void testPriority(){
        Thread thread = new Thread(() -> {
            System.out.println("Test Priority");
        });

        // 设置为最大优先级
        thread.setPriority(Thread.MAX_PRIORITY);

        thread.start();
    }
}
