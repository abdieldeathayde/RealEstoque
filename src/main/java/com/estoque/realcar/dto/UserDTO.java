package com.estoque.realcar.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO (
        @NotBlank(message = "Campo nome obrigatório")
        String username,
        @NotBlank(message = "Campo senha obrigatório")
        String password,

        String role


){
}
