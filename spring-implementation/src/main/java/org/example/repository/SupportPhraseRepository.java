package org.example.repository;

import org.example.entity.SupportPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportPhraseRepository extends JpaRepository<SupportPhrase, Long> {
    @Query("SELECT phrase.id from SupportPhrase phrase")
    List<Long> getIds();


}
