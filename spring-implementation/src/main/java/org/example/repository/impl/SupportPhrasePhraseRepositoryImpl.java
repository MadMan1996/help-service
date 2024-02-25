package org.example.repository.impl;

import org.example.entity.SupportPhrase;
import org.example.repository.SupportPhraseRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SupportPhrasePhraseRepositoryImpl implements SupportPhraseRepository {

    private final Object stub;

    private final Map<SupportPhrase, Object> supportPhrases;

    public SupportPhrasePhraseRepositoryImpl() {
        this.stub = new Object();
        this.supportPhrases = new ConcurrentHashMap<>();

        supportPhrases.put(new SupportPhrase("У тебя все получится!"), stub);
        supportPhrases.put(new SupportPhrase("Еще чуть-чуть"), stub);
        supportPhrases.put(new SupportPhrase("Сегодня ты на высоте ;)"), stub);
    }

    @Override
    public SupportPhrase getRandom() {
        int randomIndex = new Random().nextInt(supportPhrases.size());
        return supportPhrases.keySet().stream().skip(randomIndex).findFirst().orElse(new SupportPhrase("Сегодня ты на высоте ;)"));
    }
    @Override
    public void add(SupportPhrase phrase) {
        supportPhrases.put(phrase, stub);
    }


}
