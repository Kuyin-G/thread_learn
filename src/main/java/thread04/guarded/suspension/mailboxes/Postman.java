package thread04.guarded.suspension.mailboxes;

import thread04.guarded.suspension.GuardedObjectExt;

import java.util.Random;

public class Postman extends Thread{
    private Integer id;
    private String context;

    /**
     * @param id 信箱id
     * @param context 邮件内容
     * */
    public Postman(Integer id, String context){
        this.id = id;
        this.context = context;
    }
    @Override
    public void run() {
        Random random = new Random();
        int nextInt = random.nextInt(10);
        try {
            int sleepTime = nextInt * 1000;
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GuardedObjectExt guardedObjectExt = Mailboxes.getGuardedObjectExt(this.id);
        String mailContext = this.context;
        guardedObjectExt.complete(mailContext);

    }
}
