package org.example;

import java.lang.reflect.InvocationTargetException;

public interface HandlerMethod {
    void invoke(Object ...args) throws InvocationTargetException, IllegalAccessException;
}
