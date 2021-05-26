package thread03.shared.account;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;

/**
 * 测试转账
 * */
@Slf4j
public class TestTransfer {

    /**
     * 测试多次转账
     * */
    @Test
    public void testTransfer(){
        Account accountZS = new Account( 10000,"张三");
        Account accountLS = new Account( 0,"李四");

        List<Integer> transferCount =new  Vector<>(200);
        List<Thread> threads = new ArrayList<>(200);

        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(()->{
                int money = randomInt();
                accountZS.transfer(money,accountLS);
                transferCount.add(money);
            });
            thread.start();
            threads.add(thread);
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        log.info("开始计算转账结果！！！");
        int sum = transferCount.stream().mapToInt(x -> x).sum();
        log.info("总计转账：{}",sum);
        log.info(accountZS.toString());
        log.info(accountLS.toString());
        log.info("两者账户总余额：{}",(accountLS.getMoney()+accountZS.getMoney()));
    }



    static Random random = new Random();
    public int randomInt(){
        return random.nextInt(20)+1;
    }
}
