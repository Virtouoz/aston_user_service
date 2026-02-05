package com.learn.dao;

import com.learn.entity.User;
import com.learn.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final SessionFactory sessionFactory;

    public UserDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void create(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("User created: {}", user);
        } catch (ConstraintViolationException e) {
            handleRollback(transaction);
            logger.error("Constraint violation: {}", e.getMessage());
            throw new RuntimeException("Error creating user: duplicate email or invalid data");
        } catch (Exception e) {
            handleRollback(transaction);
            logger.error("Error creating user", e);
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

    @Override
    public User read(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            transaction.commit();
            if (user == null) {
                throw new RuntimeException("User not found with id: " + id);
            }
            logger.info("User read: {}", user);
            return user;
        } catch (Exception e) {
            handleRollback(transaction);
            logger.error("Error reading user", e);
            throw new RuntimeException("Error reading user: " + e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("User updated: {}", user);
        } catch (ConstraintViolationException e) {
            handleRollback(transaction);
            logger.error("Constraint violation: {}", e.getMessage());
            throw new RuntimeException("Error updating user: duplicate email or invalid data");
        } catch (Exception e) {
            handleRollback(transaction);
            logger.error("Error updating user", e);
            throw new RuntimeException("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                transaction.commit();
                logger.info("User deleted with id: {}", id);
            } else {
                throw new RuntimeException("User not found with id: " + id);
            }
        } catch (Exception e) {
            handleRollback(transaction);
            logger.error("Error deleting user", e);
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.list();
            transaction.commit();
            logger.info("Found {} users", users.size());
            return users;
        } catch (Exception e) {
            handleRollback(transaction);
            logger.error("Error finding all users", e);
            throw new RuntimeException("Error finding all users: " + e.getMessage());
        }
    }

    private void handleRollback(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}