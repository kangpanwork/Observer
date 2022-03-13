package code.core;

import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;

public class PublisherExecutor extends EventPublisher {

    /**
     * 线程池默认参数
     */
//    private static final int DEFAULT_CORE_POOL_SIZE = 10;
//    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 100;
//    private static final int DEFAULT_KEEP_ALIVE_TIME = 1;
//    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
//    private static final int DEFAULT_BLOCKING_QUEUE_CAPACITY = 100;
    /**
     * 线程池可配置参数
     */
//    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
//    private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE;
//    private long keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
//    private TimeUnit unit = DEFAULT_TIME_UNIT;

    /**
     * 有界队列
     */
    // private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(DEFAULT_BLOCKING_QUEUE_CAPACITY);

//    private ExecutorService executorService = new ThreadPoolExecutor(corePoolSize,
//            maximumPoolSize,
//            keepAliveTime,
//            unit,
//            workQueue, new ThreadFactoryBuilder()
//            .setNameFormat("PublisherExecutor" + "-%d")
//            .setDaemon(false).build(),
//            (r, executor) -> executor.shutdown());

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
                                SubscriberProxy subscriberProxy = new SubscriberProxy(obj);
                                if (obj instanceof Subscriber) {
                                    Subscriber<EventObject> target = (Subscriber<EventObject>)
                                            subscriberProxy.getProxy();
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
                                                    // 这里不立马执行，放进消息队列中执行
                                                    Event event = classType.getAnnotation(Event.class);
                                                    Thread thread = new Thread(() -> subscriber.handEvent(eventObject)
                                                            , eventObject.getClass().getSimpleName());
                                                    thread.setPriority(event.priority());
                                                    MessageQueue.messages.add(thread);
                                                    // 这里可以异步执行
                                                    // 不明白 当使用 executorService 的时候会一直执行，拒绝策略也不执行
//                                                    CompletableFuture
//                                                            .runAsync(() -> subscriber.handEvent(eventObject))
//                                                            .get(10,TimeUnit.SECONDS);
//                                                    CompletableFuture completableFuture =
//                                                            CompletableFuture.runAsync(() -> {
//                                                                        subscriber.handEvent(eventObject);
//                                                                        System.out.println(Thread.currentThread().getName());
//                                                                    },
//                                                                    executorService);
//                                                    completableFuture.join();
//                                                    completableFuture.get(19, TimeUnit.SECONDS);
                                                }
//                                            } catch (ClassNotFoundException | TimeoutException
//                                                    | InterruptedException | ExecutionException ex) {
//                                                // executorService.shutdown();
//                                                ex.printStackTrace();
//                                            }
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
