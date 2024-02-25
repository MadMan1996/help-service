package org.example.controller;

import org.example.config.property.KafkaSupportServiceProp;
import org.example.entity.SupportPhrase;
import org.example.repository.SupportPhraseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class SupportServiceIntegrationTest {
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer("latest");
    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry){
        System.out.println(registry);
        registry.add("kafka.support-service.server", kafkaContainer::getBootstrapServers);
    }

    @Autowired
    MockMvc mvc;

    @MockBean
    SupportPhraseRepository repository;

    @SpyBean
    KafkaTemplate<String, Object> commonTemplate;
    @Autowired
    KafkaSupportServiceProp props;



    @Test
    void get_support_phrase_should_return_support_phrase_from_repository() throws Exception {
        when(this.repository.getIds()).thenReturn(List.of(1L));
        when(this.repository.findById(1l)).thenReturn(Optional.of(supportPhraseWithId()));

        mvc.perform(MockMvcRequestBuilders.get("/help-service/v1/support"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phrase").value(supportPhraseWithId().getPhrase()));
    }

    @Test
    void when_send_support_phrase_to_kafka_should_consume_and_save_to_db_if_id_not_present(){
        ArgumentCaptor<SupportPhrase> supportPhraseArgumentCaptor = ArgumentCaptor.forClass(SupportPhrase.class);

        when(repository.findById(supportPhraseWithId().getId())).thenReturn(Optional.empty());
        when(repository.save(supportPhraseWithId())).thenReturn(supportPhraseWithId());

        commonTemplate.send(props.getTopic(), supportPhraseWithId());

        verify(repository, timeout(3000)).findById(supportPhraseWithId().getId());
        verify(repository).save(supportPhraseArgumentCaptor.capture());
        assertEquals(supportPhraseWithId(), supportPhraseArgumentCaptor.getValue());

    }

    @Test
    void add_support_phrase_should_save_to_repository_and_send_to_kafka() throws Exception {

        when(repository.save(supportPhraseWithoutId())).thenReturn(supportPhraseWithId());
        when(repository.findById(supportPhraseWithId().getId())).thenReturn(Optional.empty());
        doReturn(null).when(commonTemplate).send(props.getTopic(), supportPhraseWithId());

        mvc.perform((MockMvcRequestBuilders.post("/help-service/v1/support"))
                        .accept("application/json")
                        .contentType("application/json")
                        .content("""
                                 {
                                 "phrase" : "New test phrase"
                                 }
                                """))
                .andExpect(MockMvcResultMatchers.status().is(200));
        verify(repository).save(supportPhraseWithoutId());
        verify(commonTemplate).send(props.getTopic(), supportPhraseWithId());
    }

    private SupportPhrase supportPhraseWithId() {
        SupportPhrase newTestPhrase = new SupportPhrase("New test phrase");
        newTestPhrase.setId(1L);
        return newTestPhrase;
    }
    private SupportPhrase supportPhraseWithoutId() {
        return new SupportPhrase("New test phrase");
    }
}