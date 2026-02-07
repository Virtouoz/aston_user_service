package com.learn.service;

import com.learn.entity.User;

import java.util.List;

public interface UserService {
    void createUser(User user);

    User getUserById(Long id);

    void updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();
}