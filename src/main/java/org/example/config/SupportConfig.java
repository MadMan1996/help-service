package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.annotation.Configuration;
import org.example.controller.SupportController;
import org.example.controller.impl.SupportControllerImpl;
import org.example.repository.SupportPhraseRepository;
import org.example.repository.impl.SupportPhrasePhraseRepositoryImpl;
import org.example.service.SupportService;
import org.example.service.impl.SupportServiceImpl;

@Configuration
public class SupportConfig {

    public SupportPhraseRepository supportPhraseRepository(){
        return new SupportPhrasePhraseRepositoryImpl();
    }

    public SupportService supportService(SupportPhraseRepository repository){
        return new SupportServiceImpl(repository);
    }

    public SupportController supportController(SupportService supportService, ObjectMapper objectMapper){
        return new SupportControllerImpl(supportService, objectMapper);
    }
}
