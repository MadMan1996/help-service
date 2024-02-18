package org.example.infrastructure.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.infrastructure.HandlerMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerMethod implements HandlerMethod {
    private final Object controller;
    private final Method method;

    public ControllerMethod(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public void invoke(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException {
        method.invoke(controller, req, resp);
    }
}
