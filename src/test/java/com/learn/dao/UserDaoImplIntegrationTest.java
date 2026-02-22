package com.learn.dao;

import com.learn.entity.User;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImplIntegrationTest.class);

    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    static void setup() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        properties.setProperty("hibernate.connection.username", postgres.getUsername());
        properties.setProperty("hibernate.connection.password", postgres.getPassword());
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        var registry = new org.hibernate.boot.registry.StandardServiceRegistryBuilder()
                .applySettings(properties)
                .build();

        var sources = new org.hibernate.boot.MetadataSources(registry);
        sources.addAnnotatedClass(User.class);

        var metadata = sources.buildMetadata();
        sessionFactory = metadata.buildSessionFactory();
        logger.info("Test SessionFactory created");
    }

    @AfterAll
    static void teardown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("Test SessionFactory closed");
        }
    }

    @BeforeEach
    void initDao() throws NoSuchFieldException, IllegalAccessException {
        userDao = new UserDaoImpl();
        Field field = UserDaoImpl.class.getDeclaredField("sessionFactory");
        field.setAccessible(true);
        field.set(userDao, sessionFactory);
    }

    @AfterEach
    void cleanDatabase() {
        List<User> users = userDao.findAll();
        for (User user : users) {
            userDao.delete(user.getId());
        }
        logger.info("Database cleaned after test");
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
        logger.info("Create and Read integration test passed");
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
        logger.info("Update integration test passed");
    }

    @Test
    void testDelete() {
        User user = new User("To Delete", "delete@email.com", 40);
        userDao.create(user);
        Long id = user.getId();

        userDao.delete(id);

        assertThrows(RuntimeException.class, () -> userDao.read(id));
        logger.info("Delete integration test passed");
    }

    @Test
    void testFindAll() {
        User user1 = new User("User1", "user1@email.com", 20);
        User user2 = new User("User2", "user2@email.com", 21);
        userDao.create(user1);
        userDao.create(user2);

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
        logger.info("FindAll integration test passed");
    }

    @Test
    void testDuplicateEmail() {
        User user1 = new User("User1", "dup@email.com", 20);
        userDao.create(user1);

        User user2 = new User("User2", "dup@email.com", 21);
        assertThrows(RuntimeException.class, () -> userDao.create(user2));
        logger.info("Duplicate email integration test passed");
    }
}