package org.example.infrastructure;

import org.example.exception.infrastructure.HandlerNotFoundException;
import org.example.infrastructure.HandlerMethod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MappingRegistry {
    private final Map<String, HandlerMethod> handlerMethodMap;

    public MappingRegistry(){
        handlerMethodMap = new ConcurrentHashMap<>();
    }

    public void register(String mappingInfo, HandlerMethod method){
        this.handlerMethodMap.put(mappingInfo, method);
    };

    public HandlerMethod get(String mappingInfo) throws HandlerNotFoundException {
        HandlerMethod handlerMethod = handlerMethodMap.get(mappingInfo);
        if(handlerMethod == null){
            throw new HandlerNotFoundException();
        }
        return handlerMethod;
    }

}
