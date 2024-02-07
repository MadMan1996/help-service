package org.example.repository.impl;

import org.example.repository.SupportPhraseRepository;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SupportPhrasePhraseRepositoryImpl implements SupportPhraseRepository {

    private final Object stub;

    private final Map<String, Object> supportPhrases;

    public SupportPhrasePhraseRepositoryImpl() {
        this.stub = new Object();
        this.supportPhrases = new ConcurrentHashMap<>();

        supportPhrases.put("У тебя все получится!", stub);
        supportPhrases.put("Еще чуть-чуть", stub);
        supportPhrases.put("Сегодня ты на высоте ;)", stub);
    }

    @Override
    public String getRandom() {
        int randomIndex = new Random().nextInt(supportPhrases.size());
        return supportPhrases.keySet().stream().skip(randomIndex).findFirst().orElse("Помощь всегда рядом!");
    }
    @Override
    public void add(String phrase) {
        supportPhrases.put(phrase, stub);
    }


}
