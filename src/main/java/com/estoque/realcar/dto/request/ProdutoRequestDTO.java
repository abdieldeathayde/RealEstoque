package com.estoque.realcar.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoRequestDTO {

    private String nome;

    private Integer quantidade;

    private Double preco;

    // getters e setters
}