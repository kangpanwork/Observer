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
        google.getMessage().forEach(System.out::println);
    }
}
