package com.example.wordle.service;

import com.example.wordle.domain.Letter;
import com.example.wordle.domain.WordResult;
import com.example.wordle.repository.WordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WordsService {

    @Autowired
    private WordsRepository repository;

    public WordResult validateWord(String wordAttempt, String correctWord) {

        var list = repository.getWords();
        if(!list.contains(wordAttempt)) {
            return null;
        }

        Letter[] lettersResult = new Letter[correctWord.length()];

        for(int i=0; i<lettersResult.length; i++) {
            String letterAttempt = wordAttempt.substring(i, i+1);

            boolean exists = correctWord.contains(letterAttempt);
            boolean rightPlace = correctWord.charAt(i) == wordAttempt.charAt(i);

            lettersResult[i] = new Letter(letterAttempt.charAt(0), exists, rightPlace);
        }

        return new WordResult(lettersResult, Objects.equals(wordAttempt, correctWord));
    }

    public List<String> getPossibleWords(Integer wordSize, List<Letter> existingLetters, List<Letter> rightLetters, List<Letter> absentLetters) {

        List<String> wordsList = repository.getWords();

        List<String> sameSizeWords = new ArrayList<>();
        List<String> wordsWithRightLetters = new ArrayList<>();
        List<String> wordsWithExistingLetters = new ArrayList<>();
        List<String> possibleWords = new ArrayList<>();
        List<String> finalPossibleWords;

        if(!wordsList.isEmpty()) {
            for(String word : wordsList) {
                if(word.length() == wordSize) {
                    sameSizeWords.add(word);
                }
            }
        }

        if(rightLetters != null) {
            for(String word : sameSizeWords) {
                if(containsRightLetters(word, rightLetters)) {
                    wordsWithRightLetters.add(word);
                }
            }
        } else {
            wordsWithRightLetters.addAll(sameSizeWords);
        }

        if(existingLetters != null) {
            for(String word : sameSizeWords) {
                if(containsExistingLetters(word, existingLetters)) {
                    wordsWithExistingLetters.add(word);
                }
            }
        } else {
            wordsWithExistingLetters.addAll(sameSizeWords);
        }

        for(String word : sameSizeWords) {
            if(wordsWithRightLetters.contains(word) && wordsWithExistingLetters.contains(word)) {
                possibleWords.add(word);
            }
        }

        finalPossibleWords = new ArrayList<>(possibleWords);

        if(absentLetters != null) {
            for(String word : possibleWords) {
                for(Letter letter : absentLetters) {
                    if(letter.getPosition() == null) {
                        if(word.contains(String.valueOf(letter.getLetter()))) {
                            finalPossibleWords.remove(word);
                        }
                    } else if(word.charAt(letter.getPosition()) == letter.getLetter()) {
                        finalPossibleWords.remove(word);
                    }
                }
            }
        }

        return finalPossibleWords;
    }

    public static boolean containsRightLetters(String word, List<Letter> rightLetters) {
        boolean contains = true;
        for(Letter letter : rightLetters) {
            if (word.charAt(letter.getPosition()) != letter.getLetter()) {
                contains = false;
                break;
            }
        }
        return contains;
    }

    public static boolean containsExistingLetters(String word, List<Letter> existingLetters) {
        boolean contains = true;
        for(Letter letter : existingLetters) {
            if(!word.contains(String.valueOf(letter.getLetter()))) {
                contains = false;
            } else if (word.charAt(letter.getPosition()) == letter.getLetter()) {
                contains = false;
            }
        }
        return contains;
    }
}
