package com.example.wordle.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserGameInfo {
    private Long totalGames;
    private Long totalWins;
    private String percentWin;
    private List<Histogram> histogramList;
}
