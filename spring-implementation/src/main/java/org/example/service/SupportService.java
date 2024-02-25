package org.example.service;

import org.example.dto.SupportPhraseDto;
import org.example.entity.SupportPhrase;

public interface SupportService {
    SupportPhrase getSupportPhrase();
    void addSupportPhrase(SupportPhraseDto phraseDto);
}
