package thread04.guarded.suspension;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TestDownLoad {

    @Test
    public void testDownLoad() throws InterruptedException {
        GuardedObject guardedObject = new GuardedObject();
        Thread waitThread = new Thread(() -> {
            log.info("等待下载结果");
            List<String> resultList = (List<String>) guardedObject.get();
            log.info("下载结果大小：{}", resultList.size());
        }, "等待线程");


        Thread downloadThread = new Thread(() -> {
            log.info("开始下载");
            try {
                List<String> resultList = DownLoader.download();
                guardedObject.complete(resultList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "下载线程");
        downloadThread.start();
        waitThread.start();
        downloadThread.join();
        waitThread.join();
    }

    @Test
    public void testDownloadLimit() throws InterruptedException {
        GuardedObject guardedObject = new GuardedObject();
        Thread waitThread = new Thread(() -> {
            log.info("等待下载结果");
            List<String> resultList = (List<String>) guardedObject.get(1000);
            if (resultList==null){
                log.info("不能成功下载，请重试");
            }else {
                log.info("下载结果大小：{}", resultList.size());
            }
        }, "等待线程");


        Thread downloadThread = new Thread(() -> {
            log.info("开始下载");
            try {
                List<String> resultList = DownLoader.download();
                guardedObject.complete(resultList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "下载线程");
        downloadThread.start();
        waitThread.start();
        downloadThread.join();
        waitThread.join();
    }
}
