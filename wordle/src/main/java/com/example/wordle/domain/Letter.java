package com.example.wordle.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Letter {

    private char letter;
    private Integer position;
    private Boolean exists;
    private Boolean rightPlace;

    public Letter(char letter) {
        this.letter = letter;
    }

    //used to solicit tips
    public Letter(char letter, Integer position) {
        this.letter = letter;
        this.position = position;
    }

    //used for validations
    public Letter(char letter, Boolean exists, Boolean rightPlace) {
        this.letter = letter;
        this.exists = exists;
        this.rightPlace = rightPlace;
    }

    public char getLetter() {
        return this.letter;
    }

}
