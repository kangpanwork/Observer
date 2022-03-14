package code.test;

import code.baidu.Baidu;
import code.core.message.MessageProxy;
import code.core.message.MessageQueue;
import code.core.PublisherExecutor;
import code.google.Google;
import code.message.MessageInfo;
import code.message.MessageProduct;
import code.message.MessageQueueClient;


public class Main {
    public static void main(String[] args) {
        // 未分离测试
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

        // 将生产者跟消息队列分离，（订阅者未分离），生产者将消息发送给 MQ， MQ 给对应的订阅者消费
        // 先启动 Server 类的 Main 方法
        MessageQueueClient baiduMessageQueueClient = new MessageQueueClient();
        BaiduMessageProduct baiduMessageProduct = new BaiduMessageProduct();
        MessageProduct baiduMessageProductProxy = baiduMessageQueueClient.getProxy(baiduMessageProduct);
        MessageInfo baiduMessageInfo = new MessageInfo("baidu", "baidu sent message to you");
        baiduMessageProductProxy.setMessageInfo(baiduMessageInfo);

        MessageQueueClient googleMessageQueueClient = new MessageQueueClient();
        GoogleMessageProduct googleMessageProduct = new GoogleMessageProduct();
        MessageProduct googleMessageProductProxy = googleMessageQueueClient.getProxy(googleMessageProduct);
        MessageInfo googleMessageInfo = new MessageInfo("google", "google sent message to you");
        googleMessageProductProxy.setMessageInfo(googleMessageInfo);
        baiduMessageQueueClient.release();
        googleMessageQueueClient.release();
        googleMessageQueueClient.clear();

    }
}
