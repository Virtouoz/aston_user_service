package com.learn.dao;

import com.learn.entity.User;
import com.learn.util.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImplTest.class);
    private static UserDao userDao;

    @BeforeAll
    static void setup() {
        userDao = new UserDaoImpl();
        logger.info("Test setup: SessionFactory initialized");
    }

    @AfterAll
    static void teardown() {
        HibernateUtil.shutdown();
        logger.info("Test teardown: SessionFactory closed");
    }

    @BeforeEach
    void cleanDatabase() {
        // Очистка таблицы перед каждым тестом (для изоляции)
        List<User> users = userDao.findAll();
        for (User user : users) {
            userDao.delete(user.getId());
        }
        logger.info("Database cleaned before test");
    }

    @Test
    void testCreateAndRead() {
        User user = new User("Test User", "test@email.com", 25);
        userDao.create(user);
        assertNotNull(user.getId());

        User readUser = userDao.read(user.getId());
        assertEquals(user.getName(), readUser.getName());
        assertEquals(user.getEmail(), readUser.getEmail());
        assertEquals(user.getAge(), readUser.getAge());
        assertNotNull(readUser.getCreatedAt());
        logger.info("Create and Read test passed");
    }

    @Test
    void testUpdate() {
        User user = new User("Original", "orig@email.com", 30);
        userDao.create(user);

        user.setName("Updated");
        user.setEmail("updated@email.com");
        user.setAge(31);
        userDao.update(user);

        User updatedUser = userDao.read(user.getId());
        assertEquals("Updated", updatedUser.getName());
        assertEquals("updated@email.com", updatedUser.getEmail());
        assertEquals(31, updatedUser.getAge());
        logger.info("Update test passed");
    }

    @Test
    void testDelete() {
        User user = new User("To Delete", "delete@email.com", 40);
        userDao.create(user);
        Long id = user.getId();

        userDao.delete(id);

        assertThrows(RuntimeException.class, () -> userDao.read(id));
        logger.info("Delete test passed");
    }

    @Test
    void testFindAll() {
        User user1 = new User("User1", "user1@email.com", 20);
        User user2 = new User("User2", "user2@email.com", 21);
        userDao.create(user1);
        userDao.create(user2);

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
        logger.info("FindAll test passed");
    }

    @Test
    void testDuplicateEmail() {
        User user1 = new User("User1", "dup@email.com", 20);
        userDao.create(user1);

        User user2 = new User("User2", "dup@email.com", 21);
        assertThrows(RuntimeException.class, () -> userDao.create(user2));
        logger.info("Duplicate email test passed");
    }
}