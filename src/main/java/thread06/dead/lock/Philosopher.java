package thread06.dead.lock;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right){
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true){
            synchronized (left){
                log.info("获得左边的筷子：{}",left);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("等待获得右边边的筷子：{}",right);
                synchronized (right){
                    eat();
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
