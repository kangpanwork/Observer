package code.core;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

/**
 * 具体发布者
 */
public class EventPublisher implements Publisher {

    /**
     * 存储订阅者列表
     */
    public Set<Subscriber<EventObject>> subscribers = new HashSet<>();

    /**
     * 添加
     *
     * @param e 订阅者
     * @return true
     */
    @Override
    public boolean add(Subscriber<EventObject> e) {
        subscribers.add(e);
        return true;
    }

    /**
     * 移除
     *
     * @param e 订阅者
     * @return true
     */
    @Override
    public boolean remove(Subscriber<EventObject> e) {
        subscribers.remove(e);
        return true;
    }
}
