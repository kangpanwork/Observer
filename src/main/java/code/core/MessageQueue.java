package code.core;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 消息队列
 */
public class MessageQueue {
    static Comparator<Thread> cmp = Comparator.comparingInt(Thread::getPriority);
    public static BlockingQueue<Thread> messages = new PriorityBlockingQueue(100, cmp);

    /**
     * 消费消息
     */
    public static void consumptionMessage() {
        while (!messages.isEmpty()) {
            Thread thread = messages.poll();
            thread.run();
        }
    }
}