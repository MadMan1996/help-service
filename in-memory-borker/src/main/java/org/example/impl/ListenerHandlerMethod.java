package org.example.impl;

import org.example.HandlerMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ListenerHandlerMethod implements HandlerMethod {

    private final Object listenerInstance;
    private final Method handleMethod;

    public ListenerHandlerMethod(Object listenerInstance, Method handleMethod){
        this.listenerInstance = listenerInstance;
        this.handleMethod = handleMethod;
    }

    @Override
    public void invoke(Object ...args) throws InvocationTargetException, IllegalAccessException {
        handleMethod.invoke(listenerInstance, args);
    }
}
