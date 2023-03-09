package com.example.wordle.repository;

import com.example.wordle.domain.Histogram;

import java.util.List;

public interface UserWordsRepository {

    void save(Long wordId, Long userId, Integer attempts);

    Long getTotalGames(Long userId);

    Long getTotalWins(Long userId);

    List<Histogram> getHistogram(Long userId);

}
