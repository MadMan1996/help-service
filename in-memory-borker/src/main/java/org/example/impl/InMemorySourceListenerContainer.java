package org.example.impl;

import org.example.HandlerMethod;
import org.example.ListenerContainer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemorySourceListenerContainer implements ListenerContainer {

    private final Map<Class<?>, List<HandlerMethod>> listenersByEventType;

    public InMemorySourceListenerContainer() {
        listenersByEventType = new ConcurrentHashMap<>();
    }


    @Override
    public boolean add(HandlerMethod handler, Class<?> type) {
        List<HandlerMethod> handlerMethods = getCurrentHandlers(type);
        return handlerMethods.add(handler);

    }
    @Override
    public boolean add(List<HandlerMethod> handlers, Class<?> type) {
        List<HandlerMethod> handlerMethods = getCurrentHandlers(type);
        return handlerMethods.addAll(handlers);

    }

    @Override
    public boolean remove(HandlerMethod handler, Class<?> type) {
        List<HandlerMethod> handlerMethods = listenersByEventType.get(type);
        if (handlerMethods != null) {
            return handlerMethods.remove(handler);
        }
        return false;
    }

    @Override
    public boolean remove(List<HandlerMethod> handlers, Class<?> type) {
        List<HandlerMethod> handlerMethods = listenersByEventType.get(type);
        if (handlerMethods != null) {
            return handlerMethods.removeAll(handlers);
        }
        return false;
    }

    @Override
    public void notifyListeners(Object event, Class<?> type) {
        List<HandlerMethod> listeners = listenersByEventType.get(type);
        if (listeners != null) {
            listeners.forEach(listener -> {
                try {
                    listener.invoke(event);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            });
        }
    }


    @Override
    public boolean hasActiveListeners(Class<?> type) {
        return ! (listenersByEventType.get(type) == null || listenersByEventType.get(type).isEmpty());
    }

    private List<HandlerMethod> getCurrentHandlers(Class<?> type) {
        List<HandlerMethod> handlerMethods = listenersByEventType.get(type);
        if (handlerMethods == null) {
            synchronized (ListenerContainer.class) {
                handlerMethods = listenersByEventType.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>());
                listenersByEventType.put(type, handlerMethods);
            }
        }
        return handlerMethods;
    }

}
