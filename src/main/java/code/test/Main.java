package code.test;

import code.baidu.Baidu;
import code.core.MessageProxy;
import code.core.MessageQueue;
import code.core.PublisherExecutor;
import code.google.Google;


public class Main {
    public static void main(String[] args) {
        PublisherExecutor publisherExecutor = PublisherExecutor.build();
        Baidu baidu = new Baidu("Baidu");
        Google google = new Google("Google");
        MessageProxy baiduProxy = new MessageProxy(baidu, publisherExecutor);
        MessageProxy googleProxy = new MessageProxy(google, publisherExecutor);
        Baidu baiduMessage = (Baidu) baiduProxy.getProxy();
        Google googleMessage = (Google) googleProxy.getProxy();
        baiduMessage.setMessage("baidu sent message to you");
        baiduMessage.setMessage("baidu sent message to you");
        baiduMessage.setMessage("baidu sent message to you");
        googleMessage.setMessage("google sent message to you");
        MessageQueue.consumptionMessage();
    }
}
