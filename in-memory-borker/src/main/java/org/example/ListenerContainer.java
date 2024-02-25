package org.example;

import java.util.List;

public interface ListenerContainer {
    boolean add(HandlerMethod handler, Class<?> type);

    boolean add(List<HandlerMethod> handlers, Class<?> type);

    boolean remove(HandlerMethod handlerMethod, Class<?> type);

    boolean remove(List<HandlerMethod> handlerMethod, Class<?> type);

    void notifyListeners(Object event, Class<?> type);
    boolean hasActiveListeners(Class<?> type);

}
