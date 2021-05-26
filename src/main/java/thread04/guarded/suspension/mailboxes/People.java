package thread04.guarded.suspension.mailboxes;

import lombok.extern.slf4j.Slf4j;
import thread04.guarded.suspension.GuardedObjectExt;

@Slf4j
public class People extends Thread{

    private Integer id;
    private String name;

    @Override
    public void run() {
        // 收信
        GuardedObjectExt guardedObject = Mailboxes.createGuardedObjectExt();
        log.info("开始收信，邮箱id：{}",guardedObject);
        this.id = guardedObject.getId();
        Object mail = guardedObject.get();
        log.info("收到到信，id：{}",id);
        log.info("内容：{}",mail);
    }
}
