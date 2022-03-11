package code.core;

import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;

public class PublisherExecutor extends EventPublisher {

    private enum SinglePublisherExecutor {
        SINGLE_PUBLISHER_EXECUTOR;
        private final PublisherExecutor publisherExecutor;

        SinglePublisherExecutor() {
            publisherExecutor = new PublisherExecutor();
        }

        public PublisherExecutor getPublisherExecutor() {
            return publisherExecutor;
        }
    }

    public static PublisherExecutor build() {
        return SinglePublisherExecutor.SINGLE_PUBLISHER_EXECUTOR.getPublisherExecutor();
    }

    private PublisherExecutor() {
        Reflections reflections = new Reflections("code");
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(Event.class);
        Predicate<Class<?>> filter = cls -> {
            if (!Modifier.isInterface(cls.getModifiers()) && !Modifier.isAbstract(cls.getModifiers())) {
                if (EventListener.class.isAssignableFrom(cls)) {
                    return true;
                }
            }
            return false;
        };
        Optional.ofNullable(set).filter(e -> !e.isEmpty()).ifPresent(
                e -> e.stream().filter(filter).forEach(cls -> {
                    Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
                    for (int i = 0; i < declaredConstructors.length; i++) {
                        if (declaredConstructors[i].getParameterCount() == 0) {
                            try {
                                // 这里可以使用代理，当使用 JDK 代理的时候我不知道怎么获取原始类型，可以尝试使用 CGLIB 代理
                                Object obj = cls.getDeclaredConstructor().newInstance();
                                if (obj instanceof Subscriber) {
                                    Subscriber<EventObject> target = (Subscriber<EventObject>) obj;
                                    subscribers.add(target);
                                }
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                                    | NoSuchMethodException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                })
        );
    }

    public void publishEvent(EventObject eventObject) {
        Optional.ofNullable(subscribers)
                .filter(s -> !s.isEmpty())
                .ifPresent(e -> {
                            Iterator<Subscriber<EventObject>> iterator = e.stream().iterator();
                            while (iterator.hasNext()) {
                                Subscriber<EventObject> subscriber = iterator.next();
                                Class<?> oClass = subscriber.getClass();
                                Class<?> classType = isCglibProxyName(oClass) ? oClass.getSuperclass() : oClass;
                                Type[] genericInterfaces = classType.getGenericInterfaces();
                                for (int i = 0; i < genericInterfaces.length; i++) {
                                    Type type = genericInterfaces[i];
                                    if (type instanceof ParameterizedType) {
                                        Type rowType = ((ParameterizedType) type).getRawType();
                                        if ("code.core.Subscriber".equals(rowType.getTypeName())) {
                                            Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                                            Type argument = arguments != null && arguments.length > 0 ? arguments[0] : null;
                                            String name = argument.getTypeName();
                                            try {
                                                Class<?> cls =
                                                        Class.forName(name, false, getClass().getClassLoader());
                                                if (eventObject.getClass().isAssignableFrom(cls)) {
                                                    // 这里可以异步执行
                                                    subscriber.handEvent(eventObject);
                                                }
                                            } catch (ClassNotFoundException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                }

                            }
                        }
                );
    }

    private boolean isCglibProxyName(Class<?> cls) {
        String name = cls.getSimpleName();
        return name != null && name.contains("$$");
    }

}
