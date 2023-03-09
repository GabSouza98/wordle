package com.example.wordle.controller;

import com.example.wordle.domain.*;
import com.example.wordle.service.UserWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserWordsController {

    @Autowired
    private UserWordsService service;

    @PostMapping("/user-words")
    public ResponseEntity<Void> saveGameInfo(@RequestBody UserWordsDTO userWordsDTO) {
        service.save(userWordsDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user-words/info/{uuid}")
    public ResponseEntity<UserGameInfo> getUserGameInfo(@PathVariable String uuid) {
        UserGameInfo userGameInfo = service.getUserGameInfo(uuid);
        return ResponseEntity.ok(userGameInfo);
    }

}
