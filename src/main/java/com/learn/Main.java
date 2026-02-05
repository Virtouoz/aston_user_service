package com.learn;

import com.learn.dao.UserDao;
import com.learn.dao.UserDaoImpl;
import com.learn.entity.User;
import com.learn.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final UserDao userDao = new UserDaoImpl();

    public static void main(String[] args) {
        logger.info("User Service Application started");

        Scanner scanner = new Scanner(System.in);
        printMenu();

        while (true) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "create":
                        if (parts.length != 4) {
                            System.out.println("Usage: create <name> <email> <age>");
                            break;
                        }
                        String name = parts[1];
                        String email = parts[2];
                        int age = Integer.parseInt(parts[3]);
                        User newUser = new User(name, email, age);
                        userDao.create(newUser);
                        System.out.println("User created: " + newUser);
                        break;

                    case "read":
                        if (parts.length != 2) {
                            System.out.println("Usage: read <id>");
                            break;
                        }
                        Long readId = Long.parseLong(parts[1]);
                        User readUser = userDao.read(readId);
                        System.out.println("User: " + readUser);
                        break;

                    case "update":
                        if (parts.length != 5) {
                            System.out.println("Usage: update <id> <new_name> <new_email> <new_age>");
                            break;
                        }
                        Long updateId = Long.parseLong(parts[1]);
                        String newName = parts[2];
                        String newEmail = parts[3];
                        int newAge = Integer.parseInt(parts[4]);
                        User updateUser = new User(newName, newEmail, newAge);
                        updateUser.setId(updateId);
                        userDao.update(updateUser);
                        System.out.println("User updated: " + updateUser);
                        break;

                    case "delete":
                        if (parts.length != 2) {
                            System.out.println("Usage: delete <id>");
                            break;
                        }
                        Long deleteId = Long.parseLong(parts[1]);
                        userDao.delete(deleteId);
                        System.out.println("User deleted with ID: " + deleteId);
                        break;

                    case "list":
                        List<User> users = userDao.findAll();
                        if (users.isEmpty()) {
                            System.out.println("No users found.");
                        } else {
                            System.out.println("All users:");
                            for (User user : users) {
                                System.out.println(user);
                            }
                        }
                        break;

                    case "exit":
                        System.out.println("Exiting application...");
                        scanner.close();
                        HibernateUtil.shutdown();
                        logger.info("Application shutdown");
                        return;

                    default:
                        System.out.println("Unknown command. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                logger.error("Unexpected error", e);
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }

    }

    private static void printMenu() {
        System.out.println("Welcome to User Service! Available commands:");
        System.out.println("create <name> <email> <age> - Create a new user");
        System.out.println("read <id> - Read user by ID");
        System.out.println("update <id> <new_name> <new_email> <new_age> - Update user");
        System.out.println("delete <id> - Delete user by ID");
        System.out.println("list - List all users");
        System.out.println("exit - Exit the application");
    }
}