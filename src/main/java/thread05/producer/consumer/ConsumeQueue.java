package thread05.producer.consumer;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j
public class ConsumeQueue {

    private Integer capacity;
    private LinkedList<Product> list = new LinkedList<Product>();

    public ConsumeQueue(Integer capacity){
        this.capacity = capacity;
    }

    /**
     * 从消费队列中获取消费一个产品
     * */
    public Product consume(){
        synchronized (list){
            while (list.isEmpty()){
                log.info("队列为空，消费者线程等待");
                try {
                    list.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

            Product product = list.removeFirst();
            log.info("消费产品: {}",product);
            list.notifyAll();
            return product;
        }
    }

    /**
     * 将生产的产品存放到消费队列中
     * */
    public void put(Product product){
        synchronized (list){
            while (list.size()==this.capacity){
                try {
                    log.info("队列已满，生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addLast(product);
            list.notifyAll();
        }
    }
}
