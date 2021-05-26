package thread10.compare.and.setting.account;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestAccount {


    /**
     * 出现线程不安全的类：AccountUnsafe
     *      没有进行共享变量的线程安全处理
     * */
    @Test
    public void testAccountUnsafe() throws InterruptedException {
        Account account = new AccountUnsafe(1000);
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                account.withdraw(10);
            });
            list.add(thread);
            thread.start();
        }
        for (Thread thread : list) {
            thread.join();
        }
        log.info("账户余额：{}", account.getBalance());
        log.info("多线程运行结果：{}",account.getBalance()==0?"正确": "不正确");
    }


    /**
     * synchronized保证共享变量的线程安全
     * */
    @Test
    public void testAccountSafeSynchronized() throws InterruptedException {
        Account account = new AccountSafeSynchronized(1000);
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                account.withdraw(10);
            });
            list.add(thread);
            thread.start();
        }
        for (Thread thread : list) {
            thread.join();
        }
        log.info("账户余额：{}", account.getBalance());
        log.info("多线程运行结果：{}",account.getBalance()==0? "正确": "不正确");
    }

    /**
     * synchronized保证共享变量的线程安全
     * */
    @Test
    public void testAccountCAS() throws InterruptedException {
        Account account = new AccountCAS(1000);
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                account.withdraw(10);
            });
            list.add(thread);
            thread.start();
        }
        for (Thread thread : list) {
            thread.join();
        }
        log.info("账户余额：{}", account.getBalance());
        log.info("多线程运行结果：{}",account.getBalance()==0? "正确": "不正确");
    }





}
