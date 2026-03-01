# User Service

**REST API** для управления пользователями (CRUD) на **Spring Boot + Spring Data JPA**.

## ✨ Возможности

- Полноценный REST API (`POST`, `GET`, `PUT`, `DELETE`, `GET /all`)
- **DTO** вместо Entity в ответах контроллера
- Валидация данных (`@Valid` + Jakarta Validation)
- Spring Data JPA (Hibernate заменён полностью)
- Глобальная обработка исключений
- Автоматическая генерация `created_at`
- Транзакции
- Логирование в консоль + файл (с ротацией)
- Unit-тесты API на **MockMvc + Mockito**

## 🛠 Технологии

- **Java 17**
- **Spring Boot 3.3.5** (Web, Data JPA, Validation)
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **SLF4J + Logback**
- **JUnit 5 + Mockito + MockMvc** (тесты)

## 📋 Требования

- Java 17
- Maven 3.6+
- PostgreSQL (локально или в Docker)

## 🚀 Запуск

### 1. Настройка базы данных

Убедится в `src/main/resources/application.yml`, что данные верные:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
```

### 2. Сборка и запуск

```Bash
mvn clean install
mvn spring-boot:run
```

## 📌 API Endpoints

| Метод  | URL             | Описание              | Тело запроса      | Ответ                 |
|--------|-----------------|-----------------------|-------------------|-----------------------|
| POST   | /api/users      | Создать пользователя  | CreateUserRequest | UserResponseDto (201) |
| GET    | /api/users/{id} | Получить по ID        | —                 | UserResponseDto       |
| GET    | /api/users      | Получить всех         | —                 | List<UserResponseDto> |
| PUT    | /api/users/{id} | Обновить пользователя | CreateUserRequest | UserResponseDto       |
| DELETE | /api/users/{id} | Удалить пользователя  | —                 | 204 No Content        |

### Пример запроса (POST / PUT):
```json
{
  "name": "Test Test",
  "email": "test@test.com",
  "age": 30
}
```

## 🧪 Тесты

```bash
# Все тесты
mvn test

# Только тесты контроллера
mvn test -Dtest=UserControllerTest
```

### Результаты тестов:

- 7 unit-тестов контроллера (MockMvc)
- Полное покрытие всех эндпоинтов (успех + ошибки валидации + not found)

## 📁 Структура проекта

```text
src/main/java/com/learn/
├── UserServiceApplication.java
├── controller/
│   └── UserController.java
├── dto/
│   ├── CreateUserRequest.java
│   └── UserResponseDto.java
├── entity/
│   └── User.java
├── exception/
│   └── GlobalExceptionHandler.java
├── repository/
│   └── UserRepository.java
├── service/
│   ├── UserService.java
│   └── impl/
│       └── UserServiceImpl.java
└── resources/
    ├── application.yml
    └── logback.xml
```