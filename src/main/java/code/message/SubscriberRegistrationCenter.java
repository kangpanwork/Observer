package code.message;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * 订阅者注册中心
 */
public class SubscriberRegistrationCenter {
    // 这里模拟已经注册的订阅者（也是一个PRC调用，讲自己注册到里面）根据 topic 区分
    public static MessageHandler getInstance(String topic) {
        try {
            // Class code.message.SubscriberRegistrationCenter can not access a member of
            // class code.message.SubscriberRegistrationCenter$BaiduSubscriber with modifiers "private
            // return (MessageHandler) Register.getClass(topic).newInstance();
            Constructor<?> constructor = Register.getClass(topic).getDeclaredConstructor();
            constructor.setAccessible(true);
            return (MessageHandler) constructor.newInstance();

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private enum Register {

        BAIDU("baidu", BaiduSubscriber.class),
        GOOGLE("google", GoogleSubscriber.class),
        ;

        private final Class<?> cls;
        private final String topic;

        Register(String topic, Class<?> cls) {
            this.topic = topic;
            this.cls = cls;
        }

        public Class<?> getCls() {
            return cls;
        }

        public String getTopic() {
            return topic;
        }

        private static Class<?> getClass(String topic) {
            Register[] registers = Register.values();
            for (int i = 0; i < registers.length; i++) {
                if (registers[i].getTopic().equals(topic)) {
                    return registers[i].getCls();
                }
            }
           throw new RuntimeException("not fond subscriber");
        }

    }

    private static class BaiduSubscriber implements MessageHandler {

        @Override
        public void handle(String message) {
            System.out.println(message);
        }
    }

    private static class GoogleSubscriber implements MessageHandler {

        @Override
        public void handle(String message) {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        SubscriberRegistrationCenter.getInstance("baidu");
    }

}
