package thread03.shared.ticket;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@Slf4j
public class SellTicketTest {

    @Test
    public void testSellTicket()  {
        TicketWindow window  = new TicketWindow(20000);
        Vector<Integer> sellCount = new Vector<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(()->{
                int buyNumber = randomAmount();
//                log.info("本次购票：{}",buyNumber);
                int sell = window.sell(buyNumber,true);
//                log.info("本次售票：{}",sell);
                sellCount.add(sell);
            });
            thread.start();
            threads.add(thread);
        }
        threads.forEach(thread->{
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.info("余票：{}",window.getCount());
        int sum= sellCount.stream().mapToInt(x -> x).sum();
        log.info("售出票数：{}",sum);
        log.info("总票数：{}", (window.getCount()+sum));
    }

    /**
     *  Random是线程安全类
     * */
    static Random random = new Random();
    public static int randomAmount(){
        return random.nextInt(5)+1;
    }
    
}
