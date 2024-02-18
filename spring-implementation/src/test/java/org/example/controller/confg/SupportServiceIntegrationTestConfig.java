package org.example.controller.confg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.EventSource;
import org.example.Publisher;
import org.example.config.bpp.ListenerBeanPostProcessor;
import org.example.config.listener.SupportPhraseListener;
import org.example.dto.SupportPhraseDto;
import org.example.impl.InMemoryEventSourcePublisher;
import org.example.repository.SupportPhraseRepository;
import org.example.service.SupportService;
import org.example.service.impl.SupportServiceImpl;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

@TestConfiguration
public class SupportServiceIntegrationTestConfig {
    @Bean
    public Publisher<SupportPhraseDto> supportPhraseMessagePublisher(EventSource eventSource) {
        return new InMemoryEventSourcePublisher<SupportPhraseDto>(eventSource);
    }

    @Bean
    public SupportService supportService(SupportPhraseRepository sR, Publisher<SupportPhraseDto> sP){
        return new SupportServiceImpl(sR, sP);
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public SupportPhraseListener supportPhraseListener(SupportPhraseRepository sR, ObjectMapper oM){
        return new SupportPhraseListener(sR, oM);
    }

}
