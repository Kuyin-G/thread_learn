package thread02.reentrant.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class InterruptDeadThread implements Runnable{

    public static ReentrantLock lock = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    private int lockFlag;

    public InterruptDeadThread(int lockFlag){
        this.lockFlag = lockFlag;
    }

    @Override
    public void run() {

        String name = Thread.currentThread().getName();
        try {
            if(lockFlag ==1){
                lock.lockInterruptibly();
                log.info("线程:{}获得锁1资源", name);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    lock.lockInterruptibly();
                }
                lock2.lockInterruptibly();
                log.info("线程:{}获得锁2资源", name);
                System.out.println("执行通过");
            }else{
                lock2.lockInterruptibly();
                log.info("线程:{}获得锁2资源", name);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    lock2.lockInterruptibly();
                }
                lock.lockInterruptibly();
                log.info("线程:{}获得锁1资源", name);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(lock.isHeldByCurrentThread()){
                log.info("线程:{}释放锁1资源", name);
                lock.unlock();
            }
            if(lock2.isHeldByCurrentThread()){
                log.info("线程:{}释放锁2资源", name);
                lock2.unlock();
            }
        }

    }
}