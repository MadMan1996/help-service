package org.example.service.impl;

import org.example.entity.SupportPhrase;
import org.example.repository.SupportPhraseRepository;
import org.example.service.SupportService;

public class SupportServiceImpl implements SupportService {

    private final SupportPhraseRepository supportPhraseRepository;

    public SupportServiceImpl(SupportPhraseRepository repository) {
        this.supportPhraseRepository = repository;
    }
    @Override
    public SupportPhrase getSupportPhrase(){
        return supportPhraseRepository.getRandom();
    }
    @Override
    public void addSupportPhrase(SupportPhrase phrase){
        supportPhraseRepository.add(phrase);
    }


}
