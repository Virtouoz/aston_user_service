package com.learn.controller;

import com.learn.assembler.UserModelAssembler;
import com.learn.dto.CreateUserRequest;
import com.learn.dto.UserResponseDto;
import com.learn.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler assembler;

    public UserController(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя и отправляет событие CREATE в Kafka")

    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public ResponseEntity<EntityModel<UserResponseDto>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponseDto dto = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID")

    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public ResponseEntity<EntityModel<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto dto = userService.getUserById(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    public ResponseEntity<CollectionModel<EntityModel<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(assembler.toCollectionModel(users));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь обновлён")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public ResponseEntity<EntityModel<UserResponseDto>> updateUser(@PathVariable Long id,
                                                                   @Valid @RequestBody CreateUserRequest request) {
        UserResponseDto dto = userService.updateUser(id, request);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя и отправляет событие DELETE в Kafka")
    @ApiResponse(responseCode = "204", description = "Пользователь удалён")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}