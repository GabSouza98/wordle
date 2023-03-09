package com.example.wordle.service;

import com.example.wordle.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {

    @Autowired
    private UsersRepository repository;

    public String generateUUID() {
        String uuid = UUID.randomUUID().toString();
        try {
            repository.saveUuid(uuid);
        } catch (Exception e) {
            throw e;
        }

        return uuid;
    }

}
