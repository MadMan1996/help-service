package org.example.infrastructure.proxy;

import org.example.annotation.RequestMapping;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class ControllerInvocationHandler implements InvocationHandler {
    private Object instance;

    public ControllerInvocationHandler(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        OffsetDateTime startTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        if (requestMapping != null) {
            System.out.printf("%s : получен запрос %s %s \n", startTime, requestMapping.methodType(), requestMapping.path());
        }
        Object result = method.invoke(instance, args);
        if (requestMapping != null) {
            OffsetDateTime endTime = OffsetDateTime.now().truncatedTo(ChronoUnit.MILLIS);
            System.out.printf("%s : запрос %s %s обработан за %d ms \n", endTime , requestMapping.methodType(), requestMapping.path(), endTime.toInstant().toEpochMilli() - startTime.toInstant().toEpochMilli());
        }
        return result;
    }
}
