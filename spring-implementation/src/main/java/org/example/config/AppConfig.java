package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
