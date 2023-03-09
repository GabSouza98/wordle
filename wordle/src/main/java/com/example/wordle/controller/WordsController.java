package com.example.wordle.controller;

import com.example.api.WordsApi;
import com.example.wordle.domain.*;
import com.example.wordle.repository.WordsRepository;
import com.example.wordle.service.WordsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WordsController implements WordsApi {

    @Autowired
    private WordsRepository repository;

    @Autowired
    private WordsService service;

    @GetMapping("/words/all")
    public ResponseEntity<List<String>> getWords() {
        return ResponseEntity.ok(repository.getWords());
    }

    @GetMapping("/words")
    public ResponseEntity<Word> getRandomWord() {
        String word = repository.getRandomWord();
        return ResponseEntity.ok(new Word(word));
    }

    @GetMapping("/words/validations")
    public ResponseEntity<?> validateWord(@RequestParam String word, @RequestParam String correctWord) {

        if(word.length() != 5) {
            return ResponseEntity.badRequest().body("Apenas palavras de 5 letras");
        }

        var response = service.validateWord(word, correctWord);
        return nonNull(response) ? ResponseEntity.ok(response) : ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Void> saveWordsToDatabase() throws IOException {
        repository.saveWordsFromFileToDatabase(Words.getWordsList());
        return ResponseEntity.ok().build();
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void runAfterStartup() {
//        repository.saveWordsFromFileToDatabase(Words.getWordsList());
//    }

    @PostMapping("/words/tips")
    public ResponseEntity<PossibleWordsResult> getPossibleWords(@RequestBody PossibleWords possibleWords) {
        Integer wordSize = possibleWords.getWordSize();
        List<Letter> existingLetters = possibleWords.getExistingLetters();
        List<Letter> rightLetters = possibleWords.getRightLetters();
        List<Letter> absentLetters = possibleWords.getAbsentLetters();

        List<String> result = service.getPossibleWords(wordSize, existingLetters, rightLetters, absentLetters);
        return ResponseEntity.ok(new PossibleWordsResult(result));
    }
}
