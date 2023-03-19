package thread12.juc.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 * */
@Slf4j
public class SpinLock {

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock(){
        Thread thread = Thread.currentThread();
        // 如果设置不成功，就一直进行设置
        while (!atomicReference.compareAndSet(null, thread)){
        }
    }

    public void unlock(){
        Thread thread = Thread.currentThread();

        Thread lockThread = atomicReference.get();
        // 如果当前线程不持有锁，
        if(thread != lockThread){
            return;
        }

        atomicReference.compareAndSet(thread,null);
    }

    public boolean lock(TimeUnit timeUnit, long timeOut){
        // 开始时间
        long start = System.currentTimeMillis();
        // 等待超时的时间，先统一转化为毫秒
        long millis = timeUnit.toMillis(timeOut);
        Thread thread = Thread.currentThread();
        log.info("当前线程:{}", thread.getName());
        // 如果设置不成功，就一直进行设置
        while (!atomicReference.compareAndSet(null, thread)){
            // 计算等待时间
            if(System.currentTimeMillis() - start >= millis){
                log.info("超时");
                return false;
            }
        }
        return true;
    }

}
