package org.example;

public interface Publisher<T> {
    void sendEvent(T event);
}
