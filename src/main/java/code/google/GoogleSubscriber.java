package code.google;

import code.core.Event;
import code.core.Subscriber;

/**
 * 谷歌的订阅者
 */
@Event
public class GoogleSubscriber implements Subscriber<Google> {

    @Override
    public void handEvent(Google google) {
        // 非常耗时
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        google.getMessage().forEach(System.out::println);
    }
}
