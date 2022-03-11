package code.baidu;

import code.core.Event;
import code.core.Subscriber;

/**
 * 百度的订阅者
 */
@Event
public class BaiduSubscriber implements Subscriber<Baidu> {

    @Override
    public void handEvent(Baidu baidu) {
        System.out.println(baidu.getMessage());
    }
}
