package com.example.wordle.repository;

import java.util.List;

public interface WordsRepository {

    List<String> getWords();

    void saveWordsFromFileToDatabase(List<String> stringList);

    String getRandomWord();

    Long getWordId(String word);
}
