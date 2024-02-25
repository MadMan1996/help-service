package org.example.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SupportPhraseDto {
    private String phrase;

    public SupportPhraseDto() {
    }

    public SupportPhraseDto(String phrase){
        this.phrase = phrase;
    }
}
