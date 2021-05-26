package thread10.compare.and.setting.account;

public class AccountUnsafe implements Account{
    private Integer balance;
    public AccountUnsafe(Integer balance){
        this.balance = balance;
    }


    @Override
    public Integer getBalance() {
        return this.balance;
    }

    @Override
    public void withdraw(Integer amount) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.balance -= amount;
    }
}
