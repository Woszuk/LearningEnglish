package com.example.englishpolish.controller;

import com.example.englishpolish.entity.Word;
import com.example.englishpolish.exception.EmptyDatabasesException;
import com.example.englishpolish.service.WordServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class RepeatWordsController {
    private WordServiceImpl wordService;

    public RepeatWordsController(WordServiceImpl wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/write-the-meaning")
    public String repeat(HttpSession session, RedirectAttributes redirectAttributes){
        try{
            Word word = wordService.wordRepeat(session);
            session.setAttribute("word", word);
            return "repeat";
        }catch (EmptyDatabasesException e){
            redirectAttributes.addFlashAttribute("empty", "empty");
            return "redirect:/add-word";
        }
    }

    @PostMapping("/write-the-meaning")
    public String checkRepeat(){
        return "redirect:/write-the-meaning";
    }

    @GetMapping("/choose-the-meaning")
    public String choose (Model model){
        List<Word> wordList = wordService.sixWords();
        model.addAttribute("words", wordList);
        model.addAttribute("oneWord", wordService.oneWord(wordList));
        return "learn";
    }

    @PostMapping("/choose-the-meaning")
    public String choose (){
        return "redirect:/choose-the-meaning";
    }
}
