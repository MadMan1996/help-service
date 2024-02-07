package service.impl;

import repository.SupportPhraseRepository;
import service.SupportService;

public class SupportServiceImpl implements SupportService {

    private final SupportPhraseRepository supportPhraseRepository;

    public SupportServiceImpl(SupportPhraseRepository repository) {
        this.supportPhraseRepository = repository;
    }
    @Override
    public String getSupportPhrase(){
        return supportPhraseRepository.getRandom();
    }
    @Override
    public void addSupportPhrase(String phrase){
        supportPhraseRepository.add(phrase);
    }


}
