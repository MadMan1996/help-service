package org.example;

import org.example.exception.BrokerIsAlreadyRunning;
import org.example.exception.BrokerIsAlreadyStopped;

import java.util.List;

public interface EventBroker {
    boolean start() throws BrokerIsAlreadyRunning;

    boolean stop() throws BrokerIsAlreadyStopped;

    boolean subscribe(HandlerMethod listener, Class<?> type);

    boolean unsubscribe(HandlerMethod listener, Class<?> type);

    boolean subscribe(List<HandlerMethod> listeners, Class<?> type);

    boolean unsubscribe(List<HandlerMethod> listeners, Class<?> type);


}
