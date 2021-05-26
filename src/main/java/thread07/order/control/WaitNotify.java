package thread07.order.control;


public class WaitNotify {
    /**
     * 循环次数
     * */
    private int loopNumber;

    private int flag;

    public WaitNotify(int loopNumber, int flag){
        this.loopNumber = loopNumber;
        this.flag = flag;
    }

    /**
     * @param str 打印内容
     * @param waitFlag 
     * */
    public void print(String str, int waitFlag, int nextFlag){

        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while (flag != waitFlag){
                    try {
                        this.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
            System.out.print(str);
            flag = nextFlag;
            synchronized (this){
                this.notifyAll();
            }
        }
    }
}
