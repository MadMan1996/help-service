package org.example.impl;

import org.example.EventBroker;
import org.example.EventSource;
import org.example.HandlerMethod;
import org.example.ListenerContainer;
import org.example.exception.BrokerIsAlreadyRunning;
import org.example.exception.BrokerIsAlreadyStopped;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class InMemoryEventBroker implements EventBroker {
    private final EventSource eventSource;
    private final ListenerContainer listenerContainer;
    private final AtomicBoolean isRunning;
    private ExecutorService executor;

    public InMemoryEventBroker(EventSource eventSource, ListenerContainer listenerContainer) {
        this.eventSource = eventSource;
        this.listenerContainer = listenerContainer;
        isRunning = new AtomicBoolean(false);
    }

    @Override
    public boolean start() throws BrokerIsAlreadyRunning {
        if (!isRunning.get()) {
            this.executor = Executors.newSingleThreadExecutor();
            isRunning.compareAndExchange(false, true);
            executor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    eventSource.publishEvent(listenerContainer);

                }
            });
            return true;
        }
        throw new BrokerIsAlreadyRunning();
    }

    @Override
    public boolean stop() throws BrokerIsAlreadyStopped {
        if (isRunning.get()) {
            executor.shutdownNow();
            isRunning.compareAndExchange(true, false);
            return true;
        }
        throw new BrokerIsAlreadyStopped();
    }

    @Override
    public boolean subscribe(HandlerMethod listener, Class<?> type) {
        return listenerContainer.add(listener, type);
    }

    @Override
    public boolean unsubscribe(HandlerMethod listener, Class<?> type) {
        return listenerContainer.remove(listener, type);
    }

    @Override
    public boolean subscribe(List<HandlerMethod> listeners, Class<?> type) {
        return listenerContainer.add(listeners, type);
    }

    @Override
    public boolean unsubscribe(List<HandlerMethod> listeners, Class<?> type) {
        return listenerContainer.remove(listeners, type);
    }
}
