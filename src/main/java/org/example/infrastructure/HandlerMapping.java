package org.example.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.infrastructure.HandlerNotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface HandlerMapping {
    void invokeHandler(HttpServletRequest req, HttpServletResponse res) throws HandlerNotFoundException, InvocationTargetException, IllegalAccessException, IOException;
}
