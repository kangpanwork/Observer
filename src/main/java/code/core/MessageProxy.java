package code.core;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.util.EventObject;

public class MessageProxy {
    private final Object target;
    private final PublisherExecutor publisherExecutor;

    public MessageProxy(Object target, PublisherExecutor publisherExecutor) {
        this.target = target;
        this.publisherExecutor = publisherExecutor;
    }

    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new ProxyMethodInterceptor());
        enhancer.setSuperclass(target.getClass());
        enhancer.setInterfaces(target.getClass().getInterfaces());
        // 这里会清除 new 的时候构造信息
        return enhancer.create(new Class[]{String.class}, new String[]{" "});
    }

    public class ProxyMethodInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            Class<?>[] interfaces = method.getDeclaringClass().getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                Class<?> cls = interfaces[i];
                if (cls.isAssignableFrom(Message.class)) {
                   Method[] methods =  Message.class.getDeclaredMethods();
                   for (int j = 0; j < methods.length; j ++) {
                       if (methods[j].getName().equals(method.getName())
                               && methods[j].isAnnotationPresent(AutoPublish.class)) {
                           publisherExecutor.publishEvent((EventObject) target);
                           break;
                       }
                   }
                }
            }
            return method.invoke(target, objects);
        }
    }
}
