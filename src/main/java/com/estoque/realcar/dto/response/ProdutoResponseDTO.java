package com.estoque.realcar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private Integer quantidade;
    private Double preco;
}
