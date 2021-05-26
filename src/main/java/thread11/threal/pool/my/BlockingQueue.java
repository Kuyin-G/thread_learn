package thread11.threal.pool.my;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务队列
 */
public class BlockingQueue<T> {
    /**
     * 任务队列
     * */
    private Deque<T> queue = new ArrayDeque<>();

    /**
     * 锁
     * */
    private ReentrantLock lock = new ReentrantLock();


    /**
     * 生产者条件变量
     * */
    private Condition fullWaitSet = lock.newCondition();

    /**
     * 消费者条件变量
     */
    private Condition emptyWaitSet = lock.newCondition();

    /**
     * 容量
     * */
    private int capacity;

    public BlockingQueue(int capacity){
        this.capacity = capacity;
    }

    /**
     * 阻塞获取
     * @return 返回queue中的首元素
     * */
    public T take(){
        lock.lock();
        try {
            while (queue.isEmpty()){
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取队列的首元素
            T element = queue.removeFirst();
            // 获取了一个，通知fullWaitSet等待的线程，有空间了，干活了
            fullWaitSet.signal();
            return element;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞获取
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 返回queue中的首元素
     * */
    public T poll(long timeout, TimeUnit unit){
        // 将时间统一转换为纳秒
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            while (queue.isEmpty()){
                try {
                    // 预防虚假唤醒
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取队列的首元素
            T element = queue.removeFirst();
            // 获取了一个，通知fullWaitSet等待的线程，有空间了，干活了
            fullWaitSet.signal();
            return element;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 新增任务到任务队列中
     * */
    public  void put(T element){
        lock.lock();
        try {
            while (queue.size() == capacity){
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 新增的任务添加到队列尾部
            queue.addLast(element);
            // 添加了新任务，队列不为空，通知emptyWaitSet干活了
            emptyWaitSet.signal();
        }finally {
            lock.unlock();
        }
    }

    public  void tryPut(RejectPolicy<T> rejectPolicy, T task){
        lock.lock();
        try {
            // 如果队列已满，执行拒绝策略
            if (queue.size()==capacity){
                rejectPolicy.reject(this,task);
            }else {
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }
    }

    public int size(){
        return queue.size();
    }
}
