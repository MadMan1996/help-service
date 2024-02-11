package org.example;

import org.example.annotation.Configuration;
import org.example.annotation.Priority;
import org.example.annotation.Controller;
import org.example.annotation.RequestMapping;
import org.example.infrastructure.MappingRegistry;
import org.example.infrastructure.impl.ControllerMethod;
import org.example.infrastructure.proxy.ControllerInvocationHandler;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Stream;

public class ApplicationContext {
    private final Reflections reflection;
    private final Map<Class<?>, Object> INSTANCE_BY_CLASS;

    public ApplicationContext(String packageToScan) {
        this.INSTANCE_BY_CLASS = new HashMap<>();
        this.reflection = new Reflections(packageToScan);
        init();
    }

    public ApplicationContext() {
        this("org.example");
    }

    private void init() {
        reflection.getTypesAnnotatedWith(Configuration.class).stream()
                .map(this::createInstanceByClass)
                .flatMap(configInstance ->
                        getMethodsWithoutObjectsMethods(configInstance)
                                .map(method -> new Pair<Method, Object>(method, configInstance)))
                .sorted(new BeanMethodComparator())
                .forEach(pair -> initBeanByBeanMethod(pair.method, pair.instance));
    }


    public <T> T getInstance(Class<T> clazz) {
        return (T) Optional.ofNullable(this.INSTANCE_BY_CLASS.get(clazz)).orElseThrow();
    }

    private Stream<Method> getMethodsWithoutObjectsMethods(Object instance) {
        return Arrays.stream(instance.getClass().getMethods()).filter(method -> !method.getDeclaringClass().equals(Object.class));
    }

    private <T> T createInstanceByClass(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void initBeanByBeanMethod(Method beanMethod, Object configInstance) {
        Class<?> type = beanMethod.getReturnType();
        try {
            Object[] parameters = Arrays.stream(beanMethod.getParameterTypes()).map(this::getInstance).toArray();
            Object instance = beanMethod.invoke(configInstance, parameters);
            if (type.isAnnotationPresent(Controller.class)) {
                Object proxy = createControllerProxy(instance);
                registerControllerMethod(proxy, type);

            }
            INSTANCE_BY_CLASS.put(type, instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerControllerMethod(Object controller, Class<?> type) {
        Arrays.stream(type.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    MappingRegistry mappingRegistry = getInstance(MappingRegistry.class);
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    mappingRegistry.register(requestMapping.path() + requestMapping.methodType(),
                            new ControllerMethod(controller, method));
                });
    }


    private Object createControllerProxy(Object instance) {
        return Proxy.newProxyInstance(instance.getClass().getClassLoader(),
                instance.getClass().getInterfaces(), new ControllerInvocationHandler(instance));
    }

    static class Pair<K, V> {
        K method;
        V instance;

        public Pair(K method, V instance) {
            this.method = method;
            this.instance = instance;
        }
    }

    static class BeanMethodComparator implements Comparator<Pair<Method, Object>> {
        @Override
        public int compare(Pair<Method, Object> p1, Pair<Method, Object> p2) {
            return getMethodValue(p1.method) - getMethodValue(p2.method);
        }

        private int getMethodValue(Method method) {
            return method.isAnnotationPresent(Priority.class)
                    ? method.getAnnotation(Priority.class).value()
                    : method.getParameterCount();
        }
    }
}