package thread03.shared.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 账户
 * */
@AllArgsConstructor
@Getter
@Setter
@Slf4j
@ToString
public class Account {
    private int money;
    private String name;

    /**
     * 转账
     * */
    public void transfer(int transferMoney,Account targetAccount){


        if(this.money < transferMoney){
            log.info("余额不足，转账失败");
            return;
        }
        log.info("{}向{}转账{}元",this.name,targetAccount.getName(),transferMoney);
        /**
         * 一个类涉及多个实例，这里的锁对象需要使用class类
         * 这里只对主要的更新操作的代码进行加锁
         */
        synchronized (Account.class){
            this.money -= transferMoney;
            targetAccount.setMoney(targetAccount.getMoney()+transferMoney);
        }

    }

}
