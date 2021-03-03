package com.example.englishpolish.service;

import com.example.englishpolish.entity.Word;
import com.example.englishpolish.exception.EmptyDatabasesException;
import com.example.englishpolish.exception.WordAlreadyExistException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface WordService {

    List<Word> listWords(long page);

    List<Word> sixWords();

    Word oneWord (List<Word> words);

    Word wordRepeat(HttpSession session) throws EmptyDatabasesException;

    String addWord(String word) throws IOException, WordAlreadyExistException;

    long checkNumberOfPage(long page);

    long maxPage();
}
