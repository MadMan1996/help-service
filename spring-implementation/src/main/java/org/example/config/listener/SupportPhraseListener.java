package org.example.config.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.annotation.Listener;
import org.example.dto.SupportPhraseDto;
import org.example.entity.SupportPhrase;
import org.example.repository.SupportPhraseRepository;
import org.springframework.stereotype.Component;

@Component
public class SupportPhraseListener {
    private final SupportPhraseRepository repository;
    private final ObjectMapper objectMapper;

    public SupportPhraseListener(SupportPhraseRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Listener
    public void supportPhraseSubscriber(SupportPhraseDto phraseDto){
        repository.add(objectMapper.convertValue(phraseDto, SupportPhrase.class));
    }

}
