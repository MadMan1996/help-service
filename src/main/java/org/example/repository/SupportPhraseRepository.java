package org.example.repository;

import org.example.entity.SupportPhrase;

public interface SupportPhraseRepository {
    SupportPhrase getRandom();
    void add(SupportPhrase supportPhrase);
}
