package com.example.englishpolish.fixtures;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Fixture {
    private FixtureWord fixtureWord;

    public Fixture(FixtureWord fixtureWord) {
        this.fixtureWord = fixtureWord;
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void databases(){
//        fixtureWord.addWordToDatabase();
//    }
}
