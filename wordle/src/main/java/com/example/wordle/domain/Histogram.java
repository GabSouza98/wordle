package com.example.wordle.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Histogram {
    private Long attempts;
    private Long sum;
}
