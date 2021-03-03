package com.example.englishpolish.controller;

import com.example.englishpolish.exception.WordAlreadyExistException;
import com.example.englishpolish.service.WordServiceImpl;
import org.jsoup.HttpStatusException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AddWordController {
    private WordServiceImpl wordService;

    public AddWordController(WordServiceImpl wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/add-word")
    public String addWord() {
        return "add-word";
    }

    @PostMapping("/add-word")
    public String addWordFromEnglish(@RequestParam("word") String word, Model model) {
        try {
            word = wordService.addWord(word);
            model.addAttribute("success", word);
        } catch (HttpStatusException e) {
            model.addAttribute("error", word);
        } catch (WordAlreadyExistException e) {
            model.addAttribute("wordExist", word);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "add-word";
    }

}
