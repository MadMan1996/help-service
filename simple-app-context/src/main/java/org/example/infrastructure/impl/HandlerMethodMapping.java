package org.example.infrastructure.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.infrastructure.HandlerNotFoundException;
import org.example.infrastructure.HandlerMapping;
import org.example.infrastructure.HandlerMethod;
import org.example.infrastructure.MappingRegistry;

import java.lang.reflect.InvocationTargetException;

public class HandlerMethodMapping implements HandlerMapping {
    private final MappingRegistry mappingRegistry;

    public HandlerMethodMapping(MappingRegistry mappingRegistry) {
        this.mappingRegistry = mappingRegistry;
    }

    @Override
    public void invokeHandler(HttpServletRequest req, HttpServletResponse res) throws InvocationTargetException, IllegalAccessException, HandlerNotFoundException {
        String pathWithMethodType = req.getContextPath() + (req.getPathInfo() == null ? "" : req.getPathInfo()) + req.getMethod();
        HandlerMethod handlerMethod = mappingRegistry.get(pathWithMethodType);
        handlerMethod.invoke(req, res);

    }
}
