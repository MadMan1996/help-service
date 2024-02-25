package org.example.impl;

import org.example.EventSource;
import org.example.Publisher;


public class InMemoryEventSourcePublisher<T> implements Publisher<T>{
    private final EventSource eventSource;

    public InMemoryEventSourcePublisher(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    @Override
    public void sendEvent(T event) {
            eventSource.acceptEvent(event);
    }
}
