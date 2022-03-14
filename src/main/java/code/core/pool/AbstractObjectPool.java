package code.core.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractObjectPool<T> implements ObjectPool<T> {

    protected final int size;
    protected final BlockingQueue<T> queue;

    public AbstractObjectPool(int size) {
        this.size = size;
        queue = new LinkedBlockingQueue<>(size);
    }

    @Override
    public T get() {
        try {
            // 阻塞的 没有数据等待
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    @Override
    public void release(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        new Thread(() -> {
//            for (; ; ) {
//                try {
//                    // 阻塞的 异步执行 不断轮询 直到 put 成功
//                    queue.put(t);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    @Override
    public void clear() {
        queue.clear();
        System.out.println(queue.size());
    }
}
