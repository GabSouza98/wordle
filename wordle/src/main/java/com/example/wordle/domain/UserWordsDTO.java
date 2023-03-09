package com.example.wordle.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWordsDTO {

    private String word;
    private String uuid;
    private Integer attempts;

}
