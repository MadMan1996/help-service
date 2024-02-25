package org.example.service;

import org.example.entity.SupportPhrase;

public interface SupportService {
    SupportPhrase getSupportPhrase();
    void addSupportPhrase(SupportPhrase phrase);
}
