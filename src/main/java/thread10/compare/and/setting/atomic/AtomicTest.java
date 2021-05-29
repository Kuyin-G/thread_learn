package thread10.compare.and.setting.atomic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.atomic.*;

@Slf4j
public class AtomicTest {


    @Test
    public void testAtomicInteger() throws InterruptedException {
        AtomicInteger atomic = new AtomicInteger(1);

        Thread thread1 = new Thread(() -> {
            int pre = atomic.get();
            log.info("pre:{}", pre);
            int result = 2;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("修改结果？{}", (atomic.compareAndSet(pre, result) ? "成功" : "失败"));

        });

        Thread thread2 = new Thread(() -> {
            int pre = atomic.get();
            log.info("pre:{}", pre);
            int result = 3;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("修改结果？{}", (atomic.compareAndSet(pre, result) ? "成功" : "失败"));
            int andIncrement = atomic.getAndIncrement();
            log.info("getAndIncrement: {}", andIncrement);
            log.info("value:{}", atomic.get());


        });

        thread1.start();
        thread2.start();
        thread2.join();
    }

    /**
     * AtomicReference:
     *      测试使用AtomicReference
     * */
    @Test
    public void testAtomicReference() throws InterruptedException {
        AtomicReference<String> strReference = new AtomicReference<>("a");
        String value = strReference.get();
        log.info("value:{}", value);
        String modify = "b";

        /* 创建一个线程取修改值 */
        Thread modifyThread = new Thread(() -> {
            String originValue = strReference.get();
            strReference.compareAndSet(originValue, "c");
        });

        modifyThread.start();
        modifyThread.join();

        boolean success = strReference.compareAndSet(value, modify);
        log.info("修改结果：{}", success ? "success" : "failure");
        value = strReference.get();
        log.info("value:{}", value);
    }

    /**
     * 上面的AtomicReference无法解决ABA问题
     */

    @Test
    public void testABA() throws InterruptedException {
        AtomicReference<String> strReference = new AtomicReference<>("A");
        String value = strReference.get();
        log.info("value:{}", value);
        String modify = "B";

        /* 创建一个线程取修改值: A->B 然后 B->A */
        Thread modifyThread = new Thread(() -> {
            String originValue = strReference.get();
            strReference.compareAndSet(originValue, "B");
            log.info("A->B");
            strReference.compareAndSet(strReference.get(), "A");
            log.info("B->A");
        });

        modifyThread.start();
        modifyThread.join();
        log.info("主线程修改：A->B");
        boolean success = strReference.compareAndSet(value, modify);
        log.info("修改结果：{}", success ? "success" : "failure");
        value = strReference.get();
        log.info("value:{}", value);
        log.info("主线程没有察觉到modifyThread已做了如下的修改：A->B 然后 B->A");

    }

    /**
     * AtomicStampedReference:
     * 添加一个stamp(版本)支持
     */
    @Test
    public void testAtomicStampedReference() throws InterruptedException {
        AtomicStampedReference<String> stampedReference = new AtomicStampedReference<>("A", 1);
        String originValue = stampedReference.getReference();
        int stamp = stampedReference.getStamp();
        /* 创建一个线程取修改值: A->B 然后 B->A ,同时更新版本*/
        newModifyThread(stampedReference);

        int newStamp = stamp + 1;
        boolean success = stampedReference.compareAndSet(originValue, "B", stamp, newStamp);
        log.info("修改结果：{}",success? "success" :"failure");
        log.info("value: {},stamp: {}",stampedReference.getReference(),stampedReference.getStamp());
    }

    /**
     *  创建一个线程取修改值: A->B 然后 B->A ,同时更新版本
     *  */
    public void newModifyThread(AtomicStampedReference<String> stampedReference) throws InterruptedException {

        Thread modifyThread = new Thread(() -> {
            String threadOriginValue = stampedReference.getReference();
            int threadStamp = stampedReference.getStamp();
            int newStamp = threadStamp + 1;
            stampedReference.compareAndSet(threadOriginValue, "B", threadStamp, newStamp);
            log.info("A->B");
            threadStamp = stampedReference.getStamp();
            newStamp = threadStamp + 1;
            threadOriginValue = stampedReference.getReference();
            stampedReference.compareAndSet(threadOriginValue, "A", threadStamp, newStamp);
            log.info("B->A");
        });
        modifyThread.start();
        modifyThread.join();
    }

    /**
     * 原子数组:
     *      AtomicIntegerArray
     *      AtomicLongArray
     *      AtomicReferenceArray
     * */

    @Test
    public void testAtomicReferenceArray(){
        AtomicReferenceArray<Integer> referenceArray = new AtomicReferenceArray(new Integer[]{1,2,3,4,5,6,7});
        int index = 2;
        Integer expect = referenceArray.get(index);
        Integer update = 3;
        boolean success = referenceArray.compareAndSet(index, expect, update);
        log.info("修改结果：{}",success? "success" :"failure");
    }


    /**
     * 字段更新器: 利用字段更新器，可以针对象的某个域（Field）进行原子操作，只能配合volatile修饰的字段使用，否则会出现异常:java.lang.IllegalArgumentException: Must be volatile type
     *      AtomicReferenceFiledUpdater : 域 字段
     *      AtomicIntegerFieldUpdater:
     *      AtomicLongFieldUpdater:
     * */

    @Test
    public void testAtomicReferenceFiledUpdater(){
        Person person = new Person();
        AtomicReferenceFieldUpdater<Person,String> updater = AtomicReferenceFieldUpdater.newUpdater(Person.class,String.class,"name");
        updater.compareAndSet(person, null, "法外狂徒——张三");
        log.info("Person: {}",person);
    }
}
