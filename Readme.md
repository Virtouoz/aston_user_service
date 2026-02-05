# User Service

Консольное приложение для CRUD операций над пользователями в PostgreSQL с Hibernate.

## Требования
- Java 17
- Maven
- PostgreSQL (база: postgres, user: postgres)

## Запуск
1. Настрой hibernate.yml (пароль, URL).
2. `mvn clean install`
3. `mvn exec:java -Dexec.mainClass="com.learn.Main"`

## Команды
- create <name> <email> <age>
- read <id>
- update <id> <new_name> <new_email> <new_age>
- delete <id>
- list
- exit

## Тесты
`mvn test`