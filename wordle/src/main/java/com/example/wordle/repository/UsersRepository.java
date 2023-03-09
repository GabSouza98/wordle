package com.example.wordle.repository;

public interface UsersRepository {

    void saveUuid(String uuid);

    Long getUserIdByUuid(String uuid);
}
