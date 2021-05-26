package thread11.threal.pool;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

@Slf4j
public class TestForkJoin {

    @Test
    public void testForkJoin01(){
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        Integer result = forkJoinPool.invoke(new MyTask(2));
        log.info("计算结果result: {}",result);
    }
}
