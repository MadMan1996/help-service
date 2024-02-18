package org.example.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;

public interface HandlerMethod {
    void invoke(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException;
}
