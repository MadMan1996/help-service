package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.annotation.Configuration;
import org.example.annotation.Priority;
import org.example.infrastructure.HandlerMapping;
import org.example.infrastructure.impl.HandlerMethodMapping;
import org.example.infrastructure.MappingRegistry;
import org.example.infrastructure.impl.MappingRegistryByPath;

@Configuration
public class AppConfig {
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    @Priority(value = -100)
    public MappingRegistry mappingRegistry() {
        return new MappingRegistryByPath();
    }

    @Priority(value = 100)
    public HandlerMapping handlerMapping(MappingRegistry mappingRegistry) {
        return new HandlerMethodMapping(mappingRegistry);
    }

}