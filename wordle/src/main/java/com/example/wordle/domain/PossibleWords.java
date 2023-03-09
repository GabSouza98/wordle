package com.example.wordle.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PossibleWords {

    private Integer wordSize;
    private List<Letter> existingLetters;
    private List<Letter> rightLetters;
    private List<Letter> absentLetters;

}
