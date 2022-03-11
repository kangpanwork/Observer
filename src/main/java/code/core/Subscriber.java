package code.core;

import java.util.EventListener;
import java.util.EventObject;

/**
 * 订阅者
 */
public interface Subscriber<T extends EventObject> extends EventListener {
    /**
     * 处理事件
     *
     * @param t
     */
    void handEvent(T t);
}
