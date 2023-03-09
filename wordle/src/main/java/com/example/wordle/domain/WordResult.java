package com.example.wordle.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WordResult {

    private Letter[] letters;
    private Boolean success;

}
