package thread11.threal.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;


@Slf4j
public class MyTask extends RecursiveTask<Integer> {
    private int number;

    public MyTask(int number){
        this.number = number;
    }

    @Override
    protected Integer compute() {
        // 终止条件
        if(number == 1){
            return 1;
        }
        // 创建一个线程取执行任务
        MyTask task = new MyTask(number - 1);

        // 必须执行fork()方法，否则线程会join()方法无法获取结果，会进入阻塞状态
        task.fork();

        Integer join = task.join();
        log.info("分治join：{}", join);
        // 汇总任务结果
        int result = number + join;
        log.info("汇总result：{}",result);
        return result;
    }
}
