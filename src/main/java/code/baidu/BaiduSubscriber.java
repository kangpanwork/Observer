package code.baidu;

import code.core.Event;
import code.core.Subscriber;

import java.util.concurrent.TimeUnit;

/**
 * 百度的订阅者
 */
@Event
public class BaiduSubscriber implements Subscriber<Baidu> {

    @Override
    public void handEvent(Baidu baidu) {
        // 非常耗时
        try {
            Thread.sleep(7000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(baidu.getMessage());
    }
}
