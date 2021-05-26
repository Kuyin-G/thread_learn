package thread06.dead.lock;

import org.junit.Test;

public class TestDeadLock {

    /**
     * 造成死锁的原因：多个线程彼此持有对方的所资源，在等待对方释放锁
     * */
    @Test
    public void testDeadLock() throws InterruptedException {
        Object lockA = new Object();
        Object lockB = new Object();
        Thread thread1 = new Thread(new ThreadWithTwoLock(lockA, lockB));
        Thread thread2 = new Thread(new ThreadWithTwoLock(lockB, lockA));
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
     * 哲学家就餐问题
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
    public void testPhilosopherEating02(){
        // 创建5根筷子
        Chopstick_2 first = new Chopstick_2("first");
        Chopstick_2 second = new Chopstick_2("second");
        Chopstick_2 third = new Chopstick_2("third");
        Chopstick_2 fourth = new Chopstick_2("fourth");
        Chopstick_2 fifth = new Chopstick_2("fifth");
        // 创建五个哲学家

        new Philosopher_2("苏格拉底",first,second).start();
        new Philosopher_2("柏拉图",second,third).start();
        new Philosopher_2("亚里士多德",third,fourth).start();
        new Philosopher_2("赫拉克利特",fourth,fifth).start();
        new Philosopher_2("阿基米德",fifth,first).start();

        while (true){}

    }
}
