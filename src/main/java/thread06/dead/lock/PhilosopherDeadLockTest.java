package thread06.dead.lock;

import org.junit.Test;
import thread06.dead.lock.more.lock.MoreLock;
import thread06.dead.lock.philosopher.solved.ChopstickSolved;
import thread06.dead.lock.philosopher.solved.PhilosopherSolved;
import thread06.dead.lock.philosopher.unsolved.Chopstick;
import thread06.dead.lock.philosopher.unsolved.Philosopher;


/**
 * 哲学家死锁得问题
 * */
public class PhilosopherDeadLockTest {

    /**
     * 造成死锁的原因：多个线程彼此持有对方的所资源，在等待对方释放锁
     *
     * 使用排查死锁的方法： jps   jstack
     * */
    @Test
    public void testDeadLock() throws InterruptedException {
        Object lockA = new Object();
        Object lockB = new Object();
        Thread thread1 = new Thread(new MoreLock(lockA, lockB));
        Thread thread2 = new Thread(new MoreLock(lockB, lockA));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }


    /**
     * 哲学家就餐问题
     *      1、只做两件事，思考和吃饭，思考一会就吃一口放，吃完饭就接着思考
     *      2、吃饭的时候需要用两根筷子吃饭，桌上一公五根筷子，每位哲学家左右各有一根筷子
     *      3、如果筷子被身边的人拿着，就等待
     * */
    @Test
    public void testPhilosopherEating(){
        // 创建5根筷子
        Chopstick first = new Chopstick("first");
        Chopstick second = new Chopstick("second");
        Chopstick third = new Chopstick("third");
        Chopstick fourth = new Chopstick("fourth");
        Chopstick fifth = new Chopstick("fifth");
        // 创建五个哲学家

        new Philosopher("苏格拉底",first,second).start();
        new Philosopher("柏拉图",second,third).start();
        new Philosopher("亚里士多德",third,fourth).start();
        new Philosopher("赫拉克利特",fourth,fifth).start();
        new Philosopher("阿基米德",fifth,first).start();

        while (true){}


    }

    /**
     * 哲学家就餐问题得解决
     *      1、只做两件事，思考和吃饭，思考一会就吃一口放，吃完饭就接着思考
     *      2、吃饭的时候需要用两根筷子吃饭，桌上一公五根筷子，每位哲学家左右各有一根筷子
     *      3、如果筷子被身边的人拿着，就等待
     *
     * 上面的问题会出现死锁，解决死锁的问题：
     *      1、避免锁的竞争问题
     *      2、使用有限等待的方式，一旦无法获得需要的锁资源，就放弃之前已经获得的锁资源，避免进入无限等待的情况
     *          这里使用ReentrantLock的tryLock进行尝试加锁
     * */

    @Test
    public void testPhilosopherEatingSolution(){
        // 创建5根筷子
        ChopstickSolved first = new ChopstickSolved("first");
        ChopstickSolved second = new ChopstickSolved("second");
        ChopstickSolved third = new ChopstickSolved("third");
        ChopstickSolved fourth = new ChopstickSolved("fourth");
        ChopstickSolved fifth = new ChopstickSolved("fifth");
        // 创建五个哲学家

        new PhilosopherSolved("苏格拉底",first,second).start();
        new PhilosopherSolved("柏拉图",second,third).start();
        new PhilosopherSolved("亚里士多德",third,fourth).start();
        new PhilosopherSolved("赫拉克利特",fourth,fifth).start();
        new PhilosopherSolved("阿基米德",fifth,first).start();

        while (true){}

    }
}
