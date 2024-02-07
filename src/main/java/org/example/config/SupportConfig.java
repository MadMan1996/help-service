package org.example.config;

import org.example.annotation.Configuration;
import org.example.repository.SupportPhraseRepository;
import org.example.repository.impl.SupportPhrasePhraseRepositoryImpl;
import org.example.service.SupportService;
import org.example.service.impl.SupportServiceImpl;

@Configuration
public class SupportConfig {

    public SupportPhraseRepository supportPhraseRepository(){
        return new SupportPhrasePhraseRepositoryImpl();
    }

    public SupportService supportService(){
        return new SupportServiceImpl(supportPhraseRepository());
    }
}
