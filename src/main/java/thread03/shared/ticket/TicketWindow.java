package thread03.shared.ticket;

import lombok.Getter;

@Getter
public class TicketWindow {
    /**门票总数*/
    private int count;

    public TicketWindow(int count){
        this.count = count;
    }

    /**
     * 使用关键字synchronized修饰，避免线程安全问题
     * @param amount 购票数量
     * @param buy 当有票，但是票量不足的情况，是否买下余票
     * @return 返回所购买到的票
     * */
    public synchronized int sell(int amount,boolean buy){
        if(this.count >=amount) {
            this.count -= amount;
            return amount;
        }else if(this.count > 0){
            amount = this.count;
            this.count = 0;
            return amount;
        }else{
            return 0;
        }
    }
}
