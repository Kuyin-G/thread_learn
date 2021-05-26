package thread10.compare.and.setting.account;

public class AccountSafeSynchronized implements Account{


    private Integer balance;
    public AccountSafeSynchronized(Integer balance){
        this.balance = balance;
    }


    @Override
    public Integer getBalance() {
        return this.balance;
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

        synchronized (this){
            this.balance -= amount;
        }
    }
}
