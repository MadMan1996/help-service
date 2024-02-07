package org.example;

import org.example.annotation.Configuration;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
                .forEach(configInstance -> getMethodsWithoutObjectsMethods(configInstance)
                        .forEach(method -> initBeanByBeanMethod(method, configInstance)));
    }


    public <T> T getInstance(Class<T> clazz) {
        return (T) this.INSTANCE_BY_CLASS.get(clazz);
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
        Class<?> returnType = beanMethod.getReturnType();
        try {
            INSTANCE_BY_CLASS.put(returnType, beanMethod.invoke(configInstance));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
