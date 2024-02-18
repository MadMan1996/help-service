package org.example.config;

import org.example.EventBroker;
import org.example.EventSource;
import org.example.ListenerContainer;
import org.example.Publisher;
import org.example.dto.SupportPhraseDto;
import org.example.impl.InMemoryEventBroker;
import org.example.impl.InMemoryEventSourcePublisher;
import org.example.impl.InMemoryMessageSource;
import org.example.impl.InMemorySourceListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class AppConfig {
    @Bean
    public Publisher<SupportPhraseDto> supportPhraseMessagePublisher(EventSource eventSource) {
        return new InMemoryEventSourcePublisher<SupportPhraseDto>(eventSource);
    }

    @Bean
    public EventSource eventSource() {
        return new InMemoryMessageSource();
    }

    @Bean
    public ListenerContainer listenerContainer() {
        return new InMemorySourceListenerContainer();
    }

    @Bean
    @DependsOn("supportPhraseListener")
    public EventBroker eventBroker(EventSource eventSource, ListenerContainer listenerContainer) {
        return new InMemoryEventBroker(eventSource, listenerContainer);
    }
}
