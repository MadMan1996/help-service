package org.example.config.bpp;

import org.example.EventBroker;
import org.example.HandlerMethod;
import org.example.annotation.Listener;
import org.example.exception.BrokerIsAlreadyRunning;
import org.example.impl.ListenerHandlerMethod;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
@Component
public class ListenerBeanPostProcessor implements BeanPostProcessor {
    private final Map<Class<?>, List<HandlerMethod>> listeners;
    private final Set<String> brokerNames;
    public ListenerBeanPostProcessor(){
        this.listeners = new HashMap<>();
        this.brokerNames = new HashSet<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (Arrays.stream(bean.getClass().getInterfaces()).anyMatch(type -> type == EventBroker.class)){
            brokerNames.add(beanName);
        }
        Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Listener.class))
                .forEach(method -> {
                    Class<?> eventType = method.getParameterTypes()[0];
                    List<HandlerMethod> handlerMethods = listeners.computeIfAbsent(eventType, k -> new ArrayList<>());
                    handlerMethods.add(new ListenerHandlerMethod(bean, method));
                    listeners.put(eventType, handlerMethods);
                });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (brokerNames.contains(beanName)){
            EventBroker broker = (EventBroker) bean;
            listeners.forEach((type, listeners) -> broker.subscribe(listeners, type));
            try {
                broker.start();
            } catch (BrokerIsAlreadyRunning e) {
                throw new RuntimeException();
            }
        }
        return bean;
    }
}
