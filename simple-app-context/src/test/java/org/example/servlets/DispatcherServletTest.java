package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.infrastructure.HandlerNotFoundException;
import org.example.infrastructure.HandlerMapping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class DispatcherServletTest {
    @InjectMocks
    private DispatcherServlet dispatcherServlet;
    @Mock
    private HandlerMapping handlerMapping;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;


    @Test
    public void init_should_inject_hadnlerMapping() {
        dispatcherServlet.setHandlerMapping(null);

        dispatcherServlet.init();

        assertNotNull(dispatcherServlet.getHandlerMapping());

    }
    @Test
    public void service_should_redirect_req_to_handler() throws HandlerNotFoundException, InvocationTargetException, IllegalAccessException, ServletException, IOException {

        doNothing().when(handlerMapping).invokeHandler(any(HttpServletRequest.class), any(HttpServletResponse.class));

        dispatcherServlet.service(request, response);

        verify(handlerMapping, times(1)).invokeHandler(request, response);
    }

    @Test
    public void service_should_res_404_error_when_handler_not_found() throws HandlerNotFoundException, InvocationTargetException, IllegalAccessException, ServletException, IOException {
        doThrow(HandlerNotFoundException.class).when(handlerMapping).invokeHandler(any(HttpServletRequest.class), any(HttpServletResponse.class));

        dispatcherServlet.service(request, response);

        verify(handlerMapping, times(1)).invokeHandler(request, response);
        verify(response, times(1)).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

}