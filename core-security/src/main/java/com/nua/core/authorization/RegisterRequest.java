package com.nua.core.authorization;

public record RegisterRequest (
        String id,
        String email,
        String user,
        String password,
        String name
){}