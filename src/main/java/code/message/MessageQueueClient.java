package code.message;

import code.transport.HttpTransportClient;
import com.alibaba.fastjson.JSON;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MessageQueueClient {

    private final HttpTransportClient httpTransportClient;

    public MessageQueueClient() {
        this.httpTransportClient = HttpTransportClient.build();
        httpTransportClient.connect();
    }

    public void release() {
        httpTransportClient.getHttpURLConnectionPool().release(httpTransportClient.getHttpURLConnection());
    }

    public void clear() {
        httpTransportClient.getHttpURLConnectionPool().clear();
    }

    public MessageProduct getProxy(MessageProduct messageProduct) {
        return (MessageProduct) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{MessageProduct.class},
                new MessageInvocationHandler(messageProduct));

    }

    public class MessageInvocationHandler implements InvocationHandler {

        private MessageProduct messageProduct;

        public MessageInvocationHandler(MessageProduct messageProduct) {
            this.messageProduct = messageProduct;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 当生产者将设置信息体的时候，转换字节发送给 MQ
            // MQ 接受字节转换消息体，找到对应的订阅者消费
            InputStream inputStream = httpTransportClient.write(new ByteArrayInputStream(JSON.toJSONBytes(args[0])));
            // 消息消费之后确认
            byte[] bytes = IOUtils.readFully(inputStream, inputStream.available(), true);
            return JSON.parseObject(bytes, String.class);
        }
    }

}
