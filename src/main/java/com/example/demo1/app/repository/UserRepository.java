package com.example.demo1.app.repository;

import com.example.demo1.app.model.User;
import com.example.demo1.app.service.JsonDbService;

import java.io.IOException;
import java.util.List;

public class UserRepository extends BaseRepository {
    private final JsonDbService dbService;

    public UserRepository() throws IOException {
        this.dbService = new JsonDbService();
    }

    public List<User> findAll() throws IOException {
        return dbService.loadUsers();
    }
}

