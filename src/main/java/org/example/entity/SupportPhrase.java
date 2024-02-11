package org.example.entity;

import java.util.Objects;

public class SupportPhrase {
    private String phrase;

    public SupportPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getPhrase() {
        return phrase;
    }

    public SupportPhrase() {
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupportPhrase that)) return false;
        return Objects.equals(phrase, that.phrase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phrase);
    }
}
