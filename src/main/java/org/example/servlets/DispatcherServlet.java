package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ApplicationContext;
import org.example.exception.infrastructure.HandlerNotFoundException;
import org.example.infrastructure.HandlerMapping;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet extends HttpServlet {
    private HandlerMapping handlerMapping;
    @Override
    public void init() {
        ApplicationContext context = new ApplicationContext();
        this.handlerMapping = context.getInstance(HandlerMapping.class);
    }


    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            handlerMapping.invokeHandler(req, res);
        } catch (HandlerNotFoundException e) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public HandlerMapping getHandlerMapping() {
        return handlerMapping;
    }

    public void setHandlerMapping(HandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }
}
