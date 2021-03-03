package com.example.englishpolish.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String englishWord;
    private String polishWord;
    private String englishSentence;
    private String polishSentence;
    private LocalDateTime createdOn;

    public Word() {
    }

    public long getId() {
        return id;
    }

    public Word setId(long id) {
        this.id = id;
        return this;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public Word setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
        return this;
    }

    public String getPolishWord() {
        return polishWord;
    }

    public Word setPolishWord(String polishWord) {
        this.polishWord = polishWord;
        return this;
    }

    public String getEnglishSentence() {
        return englishSentence;
    }

    public Word setEnglishSentence(String englishSentence) {
        this.englishSentence = englishSentence;
        return this;
    }

    public String getPolishSentence() {
        return polishSentence;
    }

    public Word setPolishSentence(String polishSentence) {
        this.polishSentence = polishSentence;
        return this;
    }

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }
}
