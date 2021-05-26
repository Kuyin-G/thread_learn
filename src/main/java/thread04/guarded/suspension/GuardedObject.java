package thread04.guarded.suspension;

public class GuardedObject {
    protected Object response;

    /**
     * 无限等待
     * */
    public Object get(){
        synchronized (this) {
            while (response == null){
                try {
                    this.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    /**
     * 在有限的等待时间内返回
     * */
    public Object get(long timeout){
        long begin  = System.currentTimeMillis();
        long passedTime = 0;
        synchronized (this){
            while (response == null){
                long waitTime = timeout - passedTime;

                if(waitTime <= 0){
                    /**
                     * 这一轮循环应该等待的时间
                     * 为了避免虚假唤醒重复计时，
                     * 所处需要进行计算已经等待的时间
                     * */
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 更新经过的时间
                passedTime = System.currentTimeMillis() - begin;
            }
        }
        return  response;
    }

    public void complete(Object response){
        synchronized (this){
            this.response = response;
            this.notifyAll();
        }
    }

}
