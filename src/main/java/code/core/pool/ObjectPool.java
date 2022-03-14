package code.core.pool;

public interface ObjectPool<T> {

    T get();

    void release(T t);

    void clear();
}
