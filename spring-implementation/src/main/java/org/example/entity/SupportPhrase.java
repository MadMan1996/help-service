package org.example.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;

@Entity
@Table(name = "supportPhrase")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SupportPhrase {
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "support_phrase_id_seq")
    @SequenceGenerator(name = "support_phrase_id_seq", sequenceName = "support_phrase_id_seq", allocationSize = 1)
    private Long id;

    private String phrase;
    public SupportPhrase(String phrase) {
        this.phrase = phrase;
    }

    public SupportPhrase() {

    }

}
