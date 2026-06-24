package com.estoque.realcar.exception;

public class ProdutoJaExisteException extends RuntimeException {
    public ProdutoJaExisteException(String codigo) {
        super("O produto do codigo: " + codigo + " ja esta cadastrado.");
    }
}
