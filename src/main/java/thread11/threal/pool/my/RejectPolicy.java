package thread11.threal.pool.my;

/**
 * 拒绝策略
 * */
@FunctionalInterface
public interface RejectPolicy<T> {
    void reject(BlockingQueue queue, T task);
}
