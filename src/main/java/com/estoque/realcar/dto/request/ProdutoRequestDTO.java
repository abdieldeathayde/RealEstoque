package com.estoque.realcar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoRequestDTO {

    @NotBlank(message = "O codigo é obrigatorio.")
    private String codigo;

    @NotBlank(message = "O nome é obrigatorio.")
    private String nome;

    private String descricao;

    @NotNull(message = "A quantidade é obrigatorio.")
    private Integer quantidade;

    @NotNull(message = "O preco é obrigatorio.")
    private BigDecimal preco;
}