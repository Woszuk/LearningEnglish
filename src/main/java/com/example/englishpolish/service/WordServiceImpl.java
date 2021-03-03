package com.example.englishpolish.service;

import com.example.englishpolish.entity.Word;
import com.example.englishpolish.exception.EmptyDatabasesException;
import com.example.englishpolish.exception.WordAlreadyExistException;
import com.example.englishpolish.repository.WordRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class WordServiceImpl implements WordService {
    private WordRepository wordRepository;

    public WordServiceImpl(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public List<Word> listWords(long page) {
        int from = 0;
        int to = 10;
        if (page >= 2) {
            page = (page - 1) * 10;
            from += page;
        }
        return wordRepository.allWordOrderByEnglishWord(from, to);
    }

    @Override
    public Word wordRepeat(HttpSession session) throws EmptyDatabasesException {
        List<Long> idOfWordsWrite;
        if(session.getAttribute("idOfWordsWrite") == null){
            idOfWordsWrite = wordRepository.selectWordWhichPolishWordHasOneMeaning();
            session.setAttribute("idOfWordsWrite", idOfWordsWrite);
        }else {
            idOfWordsWrite = (List<Long>) session.getAttribute("idOfWordsWrite");
        }

        Random random = new Random();

        if(idOfWordsWrite.size() >=1){
            long id = idOfWordsWrite.get(random.nextInt(idOfWordsWrite.size()));
            idOfWordsWrite.remove(id);
            if(idOfWordsWrite.size() >=1){
                session.setAttribute("idOfWordsWrite", idOfWordsWrite);
            }else {
                session.removeAttribute("idOfWordsWrite");
            }
            return wordRepository.getOne(id);
        } else {
            throw new EmptyDatabasesException("Empty");
        }
    }

    @Override
    public List<Word> sixWords() {
        List<Long> idOfWordsChoose = wordRepository.selectWordWhichPolishWordHasOneMeaning();
        Random random = new Random();
        List<Word> words = new ArrayList<>();

        for(int i=0; i<6; i++) {
            long id = idOfWordsChoose.get(random.nextInt(idOfWordsChoose.size()));
            idOfWordsChoose.remove(id);
            Word word = wordRepository.getOne(id);
            String[] polishMeaning = word.getPolishWord().split(",");
            word.setPolishWord(polishMeaning[0]);
            words.add(word);
        }

        return words;
    }

    @Override
    public Word oneWord(List<Word> words) {
        Random random = new Random();
        int oneResult = random.nextInt(words.size());

        return words.get(oneResult);
    }

    @Override
    public String addWord(String word) throws IOException, WordAlreadyExistException {
        Connection connect = Jsoup.connect("https://www.diki.pl/slownik-angielskiego?q=" + word);
        Document document = connect.get();

        String dictionary = document.select("span.hide-below-xs").first().text();

        if(document.select("div.diki-results-container").select("span.hw").first() == null) {
            if (document.select("div.dictionaryCollapsedSection").select("div.dictionaryEntity").first().select("span.hw").first() != null) {
                word = document.select("div.dictionaryCollapsedSection").select("div.dictionaryEntity").first().select("span.hw").first().text();
            }
        }else {
            word = document.select("div.hws").select("span.hw").first().text();
        }

        connect = Jsoup.connect("https://www.diki.pl/slownik-angielskiego?q=" + word);
        document = connect.get();

        if (document.select("span.stopword").first() != null) {
            word = word.replaceFirst(document.select("span.stopword").first().text(), "");
            if (word.endsWith(" ")) {
                word = word.substring(0, word.length() - 1);
            }
        }

        if (dictionary.contains("angielsko-polski")) {
            addWordFromEnglishWord(word, document);
        } else {
            addWordFromPolishWord(word, document);
        }

        return word;
    }

    public void addWordFromEnglishWord(String englishWord, Document document) throws WordAlreadyExistException {
        if (wordRepository.ifExistEnglishWord(englishWord) == null) {
            Word word = new Word().setEnglishWord(englishWord);

            List<String> meanings = witchMeansWord(document);

            for (String polishWord : meanings) {
                if (word.getPolishWord() != null) {
                    word.setPolishWord(word.getPolishWord() + ", " + polishWord);
                } else {
                    word.setPolishWord(polishWord);
                }
            }

            Word word1 = ifExistExampleSentence(word, document);
            wordRepository.save(word1);
        } else {
            throw new WordAlreadyExistException("Word already exist!");
        }
    }

    public void addWordFromPolishWord(String polishWord, Document document) throws WordAlreadyExistException {
        if (wordRepository.ifExistPolishWord(polishWord) == null) {
            Word word = new Word()
                    .setPolishWord(polishWord);

            List<String> meanings = witchMeansWord(document);

            for (String englishWord : meanings) {
                if (word.getEnglishWord() != null) {
                    word.setEnglishWord(word.getEnglishWord() + ", " + englishWord);
                } else {
                    word.setEnglishWord(englishWord);
                }
            }

            Word word1 = ifExistExampleSentence(word, document);
            wordRepository.save(word1);
        } else {
            throw new WordAlreadyExistException("Word already exist!");
        }

    }

    private Word ifExistExampleSentence(Word word, Document document) {
        Word word1 = word;
        if (document.select("div.exampleSentence").size() >= 1) {
            Element exampleSentence = document.select("div.exampleSentence").get(0);
            String[] sentence = exampleSentence.text().split(" \\(");
            word1 = word1
                    .setEnglishSentence(sentence[0])
                    .setPolishSentence(sentence[1].replaceAll("\\)", ""));
        }
        return word1;
    }

    private List<String> witchMeansWord(Document document) {
        List<String> meanings = new ArrayList<>();
        Elements elements = document.select("div.diki-results-left-column").select("span.hw");

        for (Element el : elements) {
            if (el.parent().parent().hasClass("nativeToForeignMeanings") || el.parent().tag().toString().equals("h1") || el.parent().hasClass("fentry") || el.parent().hasClass("fentrymain")) {
                continue;
            } else {
                String meaning = "";
                if (document.select("span.hide-below-xs").first().text().contains("polsko-angielski")) {
                    meaning = el.text().replaceFirst(el.select("span.stopword").text(), "");
                    if (meaning.endsWith(" ")) {
                        meaning = meaning.substring(0, meaning.length() - 1);
                    }
                } else {
                    meaning = el.text();
                }

                if (meanings.size() == 0) {
                    meanings.add(meaning);
                } else {
                    boolean check = true;
                    for (int j = 0; j < meanings.size(); j++) {
                        if (meanings.get(j).equals(meaning)) {
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        meanings.add(meaning);
                    }
                }
            }
            if (meanings.size() >= 3) {
                break;
            }
        }
        return meanings;
}

    @Override
    public long maxPage() {
        long maxPage = 1;
        long counter = wordRepository.count();

        if (counter == 0){
            return maxPage;
        }

        if(counter%10 == 0) {
            maxPage = counter/10;
        } else if(counter >= 11){
            maxPage = counter/10 + 1;
        }

        return maxPage;
    }

    @Override
    public long checkNumberOfPage(long page) {
        long maxPage = maxPage();

        if (page <= 0) {
            page = 1;
        } else if (page > maxPage) {
            page = maxPage;
        }
        return page;
    }
}
