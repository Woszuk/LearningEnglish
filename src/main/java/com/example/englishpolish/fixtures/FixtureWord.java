package com.example.englishpolish.fixtures;

import com.example.englishpolish.entity.Word;
import com.example.englishpolish.repository.WordRepository;
import org.springframework.stereotype.Component;

@Component
public class FixtureWord {

    private WordRepository wordRepository;

    public FixtureWord(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public void addWordToDatabase(){
        Word word = new Word()
                .setEnglishWord("properly")
                .setPolishWord("odpowiednio, prawidłowo")
                .setEnglishSentence("These things take time if they're to be done properly.")
                .setPolishSentence("Te rzeczy zajmują dużo czasu, jeśli mają zostać wykonane prawidłowo.");

        Word word1 = new Word()
                .setEnglishWord("performance")
                .setPolishWord("wydajność, wykonanie, występ")
                .setEnglishSentence("The best performances were awarded.")
                .setPolishSentence("Najlepsze wykonania zostały nagrodzone.");

        Word word2 = new Word()
                .setEnglishWord("issue")
                .setPolishWord("kwestia, wydanie, sprawa, zagadnienie")
                .setEnglishSentence("This issue is no simpler today than it was then.")
                .setPolishSentence("Ta kwestia wcale nie jest prostsza dziś, niż była wtedy.");

        Word word3 = new Word()
                .setEnglishWord("certain")
                .setPolishWord("pewny, pewien")
                .setEnglishSentence("It's not certain where I will work.")
                .setPolishSentence("Nie jest pewne, gdzie będę pracować.");

        Word word4 = new Word()
                .setEnglishWord("approach")
                .setPolishWord("zbliżać się, podejście")
                .setEnglishSentence("Don't approach the cages, our monkeys are aggressive.")
                .setPolishSentence("Nie zbliżaj się do klatek, nasze małpy są agresywne.");

        wordRepository.save(word);
        wordRepository.save(word1);
        wordRepository.save(word2);
        wordRepository.save(word3);
        wordRepository.save(word4);
    }
}
