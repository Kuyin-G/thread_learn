package thread06.dead.lock.philosopher.solved;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhilosopherSolved extends Thread {
    ChopstickSolved left;
    ChopstickSolved right;

    public PhilosopherSolved(String name, ChopstickSolved left, ChopstickSolved right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            // 使用ReentrantLock的tryLock来让用户进行有限的等待，如果用户没有获得锁，就放弃锁

            if (left.tryLock()) {
                log.info("获得左边的筷子：{}", left);
                try {
                    log.info("等待获得右边边的筷子：{}", right);
                    if (right.tryLock()) {
                        try {
                            log.info("等待获得右边边的筷子：{}", right);
                            eat();
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }
        }
    }

    private void eat() {
        log.info("eating...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
