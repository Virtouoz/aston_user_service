# User Service

Консольное приложение для CRUD-операций над пользователями в PostgreSQL с использованием Hibernate (без Spring).

## ✨ Возможности
- Полноценный CRUD (Create, Read, Update, Delete, List)
- Автоматическая генерация `created_at` (@CreationTimestamp)
- Валидация данных в сервисном слое
- Транзакции + корректный rollback
- Логирование в консоль + файл с ротацией
- Обработка всех исключений (свои кастомные исключения)

## 🛠 Технологии
- **Java 17**
- **Hibernate 6.6** (ORM)
- **PostgreSQL**
- **Maven**
- **SLF4J + Logback** (логирование)
- **YAML** конфигурация Hibernate
- **JUnit 5 + Mockito + Testcontainers** (тесты)

## 📋 Требования
- Java 17
- Maven 3.6+
- PostgreSQL (локально или в Docker)

## 🚀 Запуск

### 1. Настройка базы данных
```yaml
# hibernate.yml
hibernate:
  connection:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres   # ← поменяй на свой
  ...
```

### 2. Сборка и запуск
```Bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.learn.Main"
```
## 📌 Доступные команды

| Команда | Описание |
|---------|----------|
|create <name> <email> <age>|Создать пользователя|
|read <id>|Показать пользователя по ID|
|update <id> <new_name> <new_email> <new_age>|Обновить пользователя|
|delete <id>|Удалить пользователя|
|list|Показать всех пользователей|
|exit|Выйти из приложения|

## 🧪 Тесты
```bash
# Только unit-тесты сервиса
mvn test -Dtest=UserServiceImplTest

# Только интеграционные тесты DAO (с Testcontainers)
mvn test -Dtest=UserDaoImplIntegrationTest

# Отчёт по покрытию кода (JaCoCo)
mvn jacoco:report
# Отчёт будет в: target/site/jacoco/index.html
```

### Результаты тестов:

- Unit-тесты (Mockito): 9 тестов
- Интеграционные тесты (Testcontainers + реальная PostgreSQL): 5 тестов
- Тесты полностью изолированы

## 📁 Структура проекта
```text
textsrc/main/java/com/learn/
├── Main.java
├── dao/
│   ├── UserDao.java
│   └── UserDaoImpl.java
├── service/
│   ├── UserService.java
│   └── impl/UserServiceImpl.java
├── entity/User.java
├── exception/
├── util/
│   ├── HibernateUtil.java
│   └── ConfigLoader.java
└── resources/
    ├── hibernate.yml
    └── logback.xml
```