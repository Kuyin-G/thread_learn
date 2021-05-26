package thread04.guarded.suspension.mailboxes;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.UUID;

@Slf4j
public class TestMail {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new People().start();
        }
        Thread.sleep(1000);
        Set<Integer> ids = Mailboxes.getIds();
        for(Integer id : ids){
            UUID uuid = UUID.randomUUID();
            String mailContext = uuid.toString();
            new Postman(id,mailContext).start();
        }

        while (true){

        }

    }
}
