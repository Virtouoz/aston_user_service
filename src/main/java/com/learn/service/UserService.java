package com.learn.service;

import com.learn.dto.CreateUserRequest;
import com.learn.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(CreateUserRequest request);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, CreateUserRequest request);

    void deleteUser(Long id);

    List<UserResponseDto> getAllUsers();
}