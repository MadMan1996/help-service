package org.example.impl;

import org.example.EventSource;
import org.example.ListenerContainer;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class  InMemoryMessageSource implements EventSource {
    private final Map<Class<?>, BlockingQueue<Object>> eventsByType;

    public InMemoryMessageSource() {
        eventsByType = new ConcurrentHashMap<>();
    }

    @Override
    public boolean acceptEvent(Object event) {
        Class<?> type = event.getClass();
        BlockingQueue<Object> events = eventsByType.get(type);
        if (events == null) {
            synchronized (InMemoryMessageSource.class) {
                events = eventsByType.get(type);
                if (events == null) {
                    events = new LinkedBlockingDeque<>();
                    eventsByType.put(type, events);
                }
            }
        }
        return events.add(event);
    }


    @Override
    public void publishEvent(ListenerContainer listenerContainer) {
        eventsByType.forEach((type, events) -> {
            if (listenerContainer.hasActiveListeners(type)) {
                Object event = events.poll();
                if (event != null) {
                    listenerContainer.notifyListeners(event, type);
                }
            }
        });
    }

}
