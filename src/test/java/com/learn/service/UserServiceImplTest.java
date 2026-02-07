package com.learn.service;

import com.learn.dao.UserDao;
import com.learn.entity.User;
import com.learn.exception.UserDaoException;
import com.learn.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateUser_Success() {
        User user = new User("Test", "test@email.com", 25);
        doNothing().when(userDao).create(any(User.class));

        userService.createUser(user);

        verify(userDao, times(1)).create(user);
    }

    @Test
    void testCreateUser_InvalidData() {
        User invalidUser = new User(null, "test@email.com", 25);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(invalidUser));
        verify(userDao, never()).create(any());
    }

    @Test
    void testGetUserById_Success() {
        Long id = 1L;
        User user = new User("Test", "test@email.com", 25);
        user.setId(id);
        when(userDao.read(id)).thenReturn(user);

        User result = userService.getUserById(id);

        assertEquals(user, result);
        verify(userDao, times(1)).read(id);
    }

    @Test
    void testGetUserById_InvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(0L));
        verify(userDao, never()).read(any());
    }

    @Test
    void testGetUserById_NotFound() {
        Long id = 1L;
        when(userDao.read(id)).thenThrow(new UserDaoException("User not found"));

        assertThrows(UserDaoException.class, () -> userService.getUserById(id));
        verify(userDao, times(1)).read(id);
    }

    @Test
    void testUpdateUser_Success() {
        User user = new User("Updated", "updated@email.com", 30);
        user.setId(1L);
        doNothing().when(userDao).update(any(User.class));

        userService.updateUser(user);

        verify(userDao, times(1)).update(user);
    }

    @Test
    void testUpdateUser_NoId() {
        User user = new User("Updated", "updated@email.com", 30);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user));
        verify(userDao, never()).update(any());
    }

    @Test
    void testDeleteUser_Success() {
        Long id = 1L;
        doNothing().when(userDao).delete(id);

        userService.deleteUser(id);

        verify(userDao, times(1)).delete(id);
    }

    @Test
    void testDeleteUser_InvalidId() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(0L));
        verify(userDao, never()).delete(any());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User("User1", "user1@email.com", 20),
                new User("User2", "user2@email.com", 21)
        );
        when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userDao, times(1)).findAll();
    }
}