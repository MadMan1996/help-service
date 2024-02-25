package org.example;

public interface EventSource {

    boolean acceptEvent(Object event);

    void publishEvent(ListenerContainer listenerContainer);

}
