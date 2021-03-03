package com.example.englishpolish.controller;

import com.example.englishpolish.entity.Word;
import com.example.englishpolish.service.WordServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {
    private WordServiceImpl wordService;

    public HomeController(WordServiceImpl wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/")
    public String home (Model model, @RequestParam(value="page", defaultValue = "1") long page, RedirectAttributes redirectAttributes){
        long maxPage = wordService.maxPage();
        page = wordService.checkNumberOfPage(page);
        List<Word> wordList = wordService.listWords(page);

        if(wordList.size() >=1){
            model.addAttribute("maxPage", maxPage);
            model.addAttribute("page", page);
            model.addAttribute("words", wordList);
            return "words";
        }else {
            redirectAttributes.addFlashAttribute("empty", "empty");
            return "redirect:/add-word";
        }
    }
}
