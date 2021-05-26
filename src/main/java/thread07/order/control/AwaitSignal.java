package thread07.order.control;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitSignal extends ReentrantLock {
    private int loopNumber;
    public AwaitSignal(int loopNumber){
        this.loopNumber = loopNumber;
    }


    public void  print(String context, Condition current,Condition next){
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await();
                System.out.print(context);
                next.signal();
            }catch (InterruptedException e){
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }

}
