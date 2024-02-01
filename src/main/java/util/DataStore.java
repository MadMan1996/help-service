package util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DataStore {
    private static final Set<String> SUPPORT_PHRASES = new HashSet<>();

    static {
        SUPPORT_PHRASES.add("У тебя все получится!");
        SUPPORT_PHRASES.add("Еще чуть-чуть");
        SUPPORT_PHRASES.add("Сегодня ты на высоте ;)");
    }

    public static String getRandomSupportPhrase() {
        int randomIndex = new Random().nextInt(SUPPORT_PHRASES.size());
        return SUPPORT_PHRASES.stream().skip(randomIndex).findFirst().orElse("Помощь всегда рядом!");
    }

    public static void addSupportPhrase(String phrase) {
        SUPPORT_PHRASES.add(phrase);
    }
    public static Set<String> getSupportPhrases(){
        return SUPPORT_PHRASES;
    }
}
