package thread08.memory.model;

public final class Singleton {
    private static volatile Singleton singleton;

    /**
     * 私有化构造方法，避免其他地方创建实例
     * */
    private  Singleton(){}

    /**
     * 对外暴露获得实例的方法，保证实例的单一
     *      使用volatile:修饰，避免再创建实例
     * */
    public static Singleton getInstance(){
        if(singleton==null){
            synchronized (Singleton.class){
                if (singleton == null){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
