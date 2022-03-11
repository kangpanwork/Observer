package code.core;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class SubscriberProxy {

    private final Object target;

    public SubscriberProxy(Object target) {
        this.target = target;
    }

    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new ProxyMethodInterceptor());
        enhancer.setSuperclass(target.getClass());
        enhancer.setInterfaces(target.getClass().getInterfaces());
        return enhancer.create();
    }

    public class ProxyMethodInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            long startTime = System.currentTimeMillis();
            Object result = method.invoke(target,objects);
            long entTime = System.currentTimeMillis();
            System.out.println("time: " + (entTime - startTime) / 1000 + "s");
            return result;
        }
    }
}
