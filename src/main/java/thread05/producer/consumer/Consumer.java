package thread05.producer.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@AllArgsConstructor
@Slf4j
public class Consumer extends Thread{

    private ConsumeQueue consumeQueue;


    @Override
    public void run() {
        while (true){
            int sleepTime = new Random().nextInt(10);
            sleepTime *= 1000;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Product consumeProduct = consumeQueue.consume();
            log.info(consumeProduct.toString());
        }
    }
}
