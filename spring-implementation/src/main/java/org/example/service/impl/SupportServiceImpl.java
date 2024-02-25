package org.example.service.impl;

import org.example.Publisher;
import org.example.dto.SupportPhraseDto;
import org.example.entity.SupportPhrase;
import org.example.repository.SupportPhraseRepository;
import org.example.service.SupportService;
import org.springframework.stereotype.Service;

@Service
public class SupportServiceImpl implements SupportService {

    private final SupportPhraseRepository supportPhraseRepository;
    private final Publisher<SupportPhraseDto> supportPhrasePublisher;

    public SupportServiceImpl(SupportPhraseRepository supportPhraseRepository, Publisher<SupportPhraseDto> supportPhrasePublisher) {
        this.supportPhraseRepository = supportPhraseRepository;
        this.supportPhrasePublisher = supportPhrasePublisher;
    }

    @Override
    public SupportPhrase getSupportPhrase(){
        return supportPhraseRepository.getRandom();
    }
    @Override
    public void addSupportPhrase(SupportPhraseDto phraseDto){
        supportPhrasePublisher.sendEvent(phraseDto);
    }


}
