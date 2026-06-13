package com.estoque.realcar.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoRequestDTO {

    private String codigo;

    private String nome;

    private Integer quantidade;

    private Double preco;

    // getters e setters
}