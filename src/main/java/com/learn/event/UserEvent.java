package com.learn.event;

public record UserEvent(String operation, String email, Long userId) {
    public static UserEvent create(String email, Long userId) {
        return new UserEvent("CREATE", email, userId);
    }
    public static UserEvent delete(String email, Long userId) {
        return new UserEvent("DELETE", email, userId);
    }
}
