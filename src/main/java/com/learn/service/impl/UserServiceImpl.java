package com.learn.service.impl;

import com.learn.dao.UserDao;
import com.learn.entity.User;
import com.learn.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(User user) {
        // Валидация: имя, email не null, возраст > 0
        if (user.getName() == null || user.getEmail() == null || user.getAge() <= 0) {
            throw new IllegalArgumentException("Invalid user data");
        }
        userDao.create(user);
    }

    @Override
    public User getUserById(Long id) {
        // Валидация: ID не null и > 0
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return userDao.read(id);
    }

    @Override
    public void updateUser(User user) {
        // Валидация: ID обязателен для обновления
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID is required for update");
        }
        userDao.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        // Валидация: ID не null и > 0
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        userDao.delete(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
}