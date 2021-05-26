package thread10.compare.and.setting.account;

import java.util.concurrent.atomic.AtomicInteger;

public class AccountCAS implements Account{
    private AtomicInteger balance;
    public AccountCAS(Integer balance){
        this.balance = new AtomicInteger(balance);
    }


    @Override
    public Integer getBalance() {
        return balance.get();
    }

    /**
     * 使用synchronized关键字来保证代码的线程安全性
     * */
    @Override
    public void withdraw(Integer amount) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true){
            int value = balance.get();
            int result = value - amount;
            if(balance.compareAndSet(value, result)){
                break;
            }
        }

    }
}
