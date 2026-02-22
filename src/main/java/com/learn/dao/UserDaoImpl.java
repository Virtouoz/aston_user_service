package com.learn.dao;

import com.learn.entity.User;
import com.learn.exception.UserDaoException;
import com.learn.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final SessionFactory sessionFactory;

    public UserDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(User user) {
        executeTransaction("creating", session -> {
            session.persist(user);
            return user;
        }, result -> logger.info("User created: {}", result));
    }

    @Override
    public User read(Long id) {
        return executeTransaction("reading", session -> {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new UserDaoException("User not found with id: " + id);
            }
            return user;
        }, result -> logger.info("User read: {}", result));
    }

    @Override
    public void update(User user) {
        executeTransaction("updating", session -> {
            session.merge(user);
            return user;
        }, result -> logger.info("User updated: {}", result));
    }

    @Override
    public void delete(Long id) {
        executeTransaction("deleting", session -> {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new UserDaoException("User not found with id: " + id);
            }
            session.remove(user);
            return id;
        }, result -> logger.info("User deleted with id: {}", result));
    }

    @Override
    public List<User> findAll() {
        return executeTransaction("finding all", session -> {
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.list();
        }, result -> logger.info("Found {} users", result.size()));
    }

    private <T> T executeTransaction(String operation, Function<Session, T> action, Consumer<T> postSuccess) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T result = action.apply(session);
            transaction.commit();
            postSuccess.accept(result);
            return result;
        } catch (Exception e) {
            handleRollback(transaction);
            if (e instanceof ConstraintViolationException) {
                logger.error("Constraint violation in {} user: {}", operation, e.getMessage(), e);
                throw new UserDaoException("Error " + operation + " user: duplicate email or invalid data", e);
            } else {
                logger.error("Error {} user", operation, e);
                throw new UserDaoException("Error " + operation + " user", e);
            }
        }
    }

    private void handleRollback(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}