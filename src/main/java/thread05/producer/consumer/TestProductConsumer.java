package thread05.producer.consumer;

public class TestProductConsumer {

    public static void main(String[] args) throws InterruptedException {
        ConsumeQueue queue = new ConsumeQueue(10);

        // 创建三个生产者
        for (int i = 0; i < 3; i++) {
            new Producer(queue).start();
        }

        Consumer consumer = new Consumer(queue);
        consumer.start();
        consumer.join();
    }





}
