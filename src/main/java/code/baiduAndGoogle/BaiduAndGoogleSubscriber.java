package code.baiduAndGoogle;

import code.core.MultipleEvent;
import code.core.Subscriber;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.EventObject;
// 无法获取 name 的值
@MultipleEvent
public class BaiduAndGoogleSubscriber
        implements Subscriber<@code.core.EventObject(name = {"code.Baidu", "code.Google"}) EventObject> {

    @Override
    public void handEvent(EventObject eventObject) {
        System.out.println(eventObject.getSource());
    }

    public static void main(String[] args) {
        Type[] types = BaiduAndGoogleSubscriber.class.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] ts = parameterizedType.getActualTypeArguments();
                for (Type t : ts) {
                    if (t instanceof Class) {
                        Class cls = (Class) t;
                        TypeVariable[] typeVariables = cls.getTypeParameters();
                        Annotation[] annotations = cls.getAnnotations();
                        for(Annotation annotation : annotations) {
                            System.out.println(annotation.annotationType());
                        }
                    }
                    System.out.println(t.getTypeName());
                }
            }
        }
    }
}
