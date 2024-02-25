package org.example.infrastructure.impl;

import org.example.exception.infrastructure.HandlerNotFoundException;
import org.example.infrastructure.HandlerMethod;
import org.example.infrastructure.MappingRegistry;

public class MappingRegistryByPath extends MappingRegistry {
    public MappingRegistryByPath() {
        super();
    }

    @Override
    public void register(String mappingInfo, HandlerMethod method) {
        super.register(mappingInfo, method);
    }

    @Override
    public HandlerMethod get(String mappingInfo) throws HandlerNotFoundException {
        return super.get(mappingInfo);
    }
}

