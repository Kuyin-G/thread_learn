package thread04.guarded.suspension.mailboxes;

import thread04.guarded.suspension.GuardedObjectExt;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class Mailboxes {

    private static Map<Integer, GuardedObjectExt> boxes = new Hashtable<>();
    private static int id = 1;
    /**
     * @return 产生唯一Id
     * */
    private  static synchronized int generateId(){
        return id++;
    }

    public static GuardedObjectExt createGuardedObjectExt(){
        int newId = generateId();
        GuardedObjectExt guardedObjectExt = new GuardedObjectExt(newId);
        boxes.put(newId,guardedObjectExt);
        return guardedObjectExt;
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }
    public static GuardedObjectExt getGuardedObjectExt(int id){
        return boxes.remove(id);
    }
}
