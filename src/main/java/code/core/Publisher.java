package code.core;

import java.util.EventObject;

/**
 * 事件发布者，支持添加和移除订阅者
 */
public interface Publisher {
    boolean add(Subscriber<EventObject> e);
    boolean remove(Subscriber<EventObject> e);
}
