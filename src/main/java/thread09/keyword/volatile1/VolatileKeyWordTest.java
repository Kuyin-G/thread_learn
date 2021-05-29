package thread09.keyword.volatile1;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class VolatileKeyWordTest {

    /**
     * 关键字 ： volatile
     *      1、保证可见性
     *      2、保证有序性
     *      3、不保证原子性
     *
     *      原理：
     *          read 是volatile修饰的变量,
     *              读取值带读屏障：读取值带读屏障，会将之后需要读取的变量从主内存中读取
     *              修改值带写屏障：修改值的时候，带写屏障，会将之前修改的变量值写回到主内存中
     * */

    static volatile boolean ready = false;
    static int num = 0;

    @Test
    public void testVolatile() throws InterruptedException {

        Thread readThread = new Thread(()->{
            for (int i = 0; i < 5; i++) {
                LockSupport.park();
                read();
            }
        });

        Thread writeThread = new Thread(()->{
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                    write(true);
                    LockSupport.unpark(readThread);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        readThread.start();
        writeThread.start();
        readThread.join();

    }


    /**
     * read 是volatile修饰的变量, 读取值带读屏障，会将之后需要读取的变量从主内存中读取
     * */
    public static void read(){
        //
        if(ready){
            // 读屏障
            log.info("读线程读取到value:{}", num );
        }else {
            log.info("value: {}",1);
        }
    }

    /**
     * read 是volatile修饰的变量, 修改值的时候，带写屏障，会将之前修改的变量值写回到主内存中
     * */
    public static void write(boolean value){
        num++;
        log.info("写线程修改为value:{}",num);
        ready = value;
    }
}
