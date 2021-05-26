package thread05.producer.consumer;


import lombok.AllArgsConstructor;

import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
public class Producer extends Thread{

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
            Product product = new Product();
            product.setValue(UUID.randomUUID().toString());
            consumeQueue.put(product);
        }
    }
}
