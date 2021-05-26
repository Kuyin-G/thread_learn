package thread06.dead.lock;


import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 *ThreadWithTwoLock : 具备多个锁对象的线程
 * */
@Slf4j
@AllArgsConstructor
public class ThreadWithTwoLock implements Runnable{
    private Object lockObjectA;
    private Object lockObjectB;

    @SneakyThrows
    @Override
    public void run() {
        synchronized (lockObjectA){
            String name = Thread.currentThread().getName();
            log.info("线程{}获取锁资源{}",name,lockObjectA);
            Thread.sleep(1000);
            log.info("线程{}等待锁资源{}",name,lockObjectB);
            synchronized (lockObjectB){
                log.info("线程{}获取锁资源{}",name,lockObjectB);
            }
        }
    }
}
