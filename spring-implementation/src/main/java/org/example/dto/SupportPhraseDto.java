package org.example.dto;

import java.util.Objects;

public class SupportPhraseDto {
    private String phrase;

    public SupportPhraseDto(String phrase){
        this.phrase = phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getPhrase() {
        return phrase;
    }

    public SupportPhraseDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupportPhraseDto that)) return false;
        return Objects.equals(phrase, that.phrase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phrase);
    }

    @Override
    public String toString() {
        return "SupportPhraseDto{" +
                "phrase='" + phrase + '\'' +
                '}';
    }
}
