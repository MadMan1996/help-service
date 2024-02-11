package org.example.infrastructure.impl;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.infrastructure.HandlerNotFoundException;
import org.example.infrastructure.HandlerMethod;
import org.example.infrastructure.MappingRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class HandlerMethodMappingTest {
    @InjectMocks
    private HandlerMethodMapping handlerMethodMapping;
    @Mock
    private MappingRegistry mappingRegistry;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HandlerMethod handlerMethod;

    @Test
    public void invokeHandler_should_invoke_handlerMethod_when_it_is_present() throws InvocationTargetException, IllegalAccessException, HandlerNotFoundException {
        when(request.getContextPath()).thenReturn("/help-service");
        when(request.getPathInfo()).thenReturn("/v1/support");
        when(request.getMethod()).thenReturn("GET");
        when(mappingRegistry.get("/help-service/v1/supportGET")).thenReturn(handlerMethod);

        handlerMethodMapping.invokeHandler(request, response);

        verify(handlerMethod, times(1)).invoke(request, response);
    }

    @Test
    public void invokeHandler_should_throw_HandlerNotFoundExcetption_when_there_is_no_handler() throws HandlerNotFoundException {
        when(request.getContextPath()).thenReturn("/help-service");
        when(request.getPathInfo()).thenReturn("/v1/support");
        when(request.getMethod()).thenReturn("GET");
        doThrow(HandlerNotFoundException.class).when(mappingRegistry).get("/help-service/v1/supportGET");

        assertThrows(HandlerNotFoundException.class, ()-> handlerMethodMapping.invokeHandler(request, response));
    }
}