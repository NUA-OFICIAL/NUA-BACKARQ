package com.nua.core.authorization;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @NotBlank
        @Schema(description = "Username", example = "usuario1")
        String user,
        @NotBlank
        @Schema(description = "Contraseña del usuario", example = "hfekhfewuh")
        String password
){
}