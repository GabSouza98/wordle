package com.example.wordle.controller;

import com.example.wordle.domain.UuidDTO;
import com.example.wordle.repository.UsersRepository;
import com.example.wordle.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class UsersController {

    @Autowired
    private UsersService service;

    @Autowired
    private UsersRepository repository;

    @GetMapping("/users/uuid")
    public ResponseEntity<UuidDTO> generateUuid() {
        return ResponseEntity.ok(new UuidDTO(service.generateUUID()));
    }




}
