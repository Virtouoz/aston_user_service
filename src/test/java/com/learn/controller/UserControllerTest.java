package com.learn.controller;

import com.learn.dto.CreateUserRequest;
import com.learn.dto.UserResponseDto;
import com.learn.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final UserResponseDto sampleUser = new UserResponseDto(
            1L, "Test User", "test@email.com", 25, Timestamp.from(Instant.now())
    );

    @Test
    void createUser_Success() throws Exception {
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Test User",
                                    "email": "test@email.com",
                                    "age": 25
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    void createUser_ValidationError() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "",
                                    "email": "invalid-email",
                                    "age": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.age").exists());
    }

    @Test
    void getUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(sampleUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById(999L))
                .thenThrow(new RuntimeException("User not found with id: 999"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found with id: 999"));
    }

    @Test
    void updateUser_Success() throws Exception {
        when(userService.updateUser(eq(1L), any(CreateUserRequest.class))).thenReturn(sampleUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated User",
                                    "email": "updated@email.com",
                                    "age": 30
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User")); // возвращается обновлённый
    }

    @Test
    void getAllUsers_Success() throws Exception {
        List<UserResponseDto> users = Arrays.asList(
                sampleUser,
                new UserResponseDto(2L, "User Two", "two@email.com", 30, Timestamp.from(Instant.now()))
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}