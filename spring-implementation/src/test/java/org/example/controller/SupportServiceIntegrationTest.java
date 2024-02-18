package org.example.controller;

import org.example.controller.confg.InMemoryBrockerTestConfig;
import org.example.controller.confg.SupportServiceIntegrationTestConfig;
import org.example.entity.SupportPhrase;
import org.example.repository.SupportPhraseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(SupportController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InMemoryBrockerTestConfig.class, SupportServiceIntegrationTestConfig.class})
class SupportServiceIntegrationTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    SupportPhraseRepository repository;

    @Test
    void get_support_phrase_should_return_support_phrase_from_repository() throws Exception {
        Mockito.when(this.repository.getRandom()).thenReturn(supportPhrase());

        mvc.perform(MockMvcRequestBuilders.get("/help-service/v1/support"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phrase").value(supportPhrase().getPhrase()));
    }

    @Test
    void add_support_phrase_should_save_this_phrase_to_repository() throws Exception {
        ArgumentCaptor<SupportPhrase> supportPhraseArgumentCaptor = ArgumentCaptor.forClass(SupportPhrase.class);
        Mockito.doNothing().when(repository).add(any(SupportPhrase.class));

        mvc.perform((MockMvcRequestBuilders.post("/help-service/v1/support"))
                        .accept("application/json")
                        .contentType("application/json")
                        .content("""
                                 {
                                 "phrase" : "New test phrase"
                                 }
                                """))
                .andExpect(MockMvcResultMatchers.status().is(200));

        Mockito.verify(repository, Mockito.times(1)).add(supportPhraseArgumentCaptor.capture());
        assertEquals(supportPhrase(), supportPhraseArgumentCaptor.getValue());
    }

    private SupportPhrase supportPhrase() {
        return new SupportPhrase("New test phrase");
    }
}