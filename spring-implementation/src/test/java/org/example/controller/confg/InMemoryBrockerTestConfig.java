package org.example.controller.confg;

import org.example.EventBroker;
import org.example.EventSource;
import org.example.ListenerContainer;
import org.example.config.bpp.ListenerBeanPostProcessor;
import org.example.impl.InMemoryEventBroker;
import org.example.impl.InMemoryMessageSource;
import org.example.impl.InMemorySourceListenerContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
@TestConfiguration
public class InMemoryBrockerTestConfig {

    @Bean
    public ListenerBeanPostProcessor listenerBeanPostProcessor(){
        return new ListenerBeanPostProcessor();
    }

    @Bean
    public EventSource eventSource() {
        return new InMemoryMessageSource();
    }

    @Bean
    public ListenerContainer listenerContainer() {
        return new InMemorySourceListenerContainer();
    }

    @Bean()
    @DependsOn("supportPhraseListener")
    public EventBroker eventBroker(EventSource eventSource, ListenerContainer listenerContainer) {
        return new InMemoryEventBroker(eventSource, listenerContainer);
    }

}
