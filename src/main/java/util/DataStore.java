package util;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private static DataStore insance;
    private static final Object stub = new Object();

    public static DataStore getInstance() {
        if (insance == null)
            synchronized (DataStore.class) {
                if (insance == null) {
                    insance = new DataStore();
                }
            }
        return insance;
    }


    private final Map<String, Object> supportPhrases;

    private DataStore() {
        this.supportPhrases = new ConcurrentHashMap<>();
        supportPhrases.put("У тебя все получится!", stub);
        supportPhrases.put("Еще чуть-чуть", stub);
        supportPhrases.put("Сегодня ты на высоте ;)", stub);
    }


    public String getRandomSupportPhrase() {
        int randomIndex = new Random().nextInt(supportPhrases.size());
        return supportPhrases.keySet().stream().skip(randomIndex).findFirst().orElse("Помощь всегда рядом!");
    }

    public void addSupportPhrase(String phrase) {
        supportPhrases.put(phrase, stub);
    }

    public Set<String> getSupportPhrases() {
        return new HashSet<>(supportPhrases.keySet());
    }

}
