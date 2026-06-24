package com.estoque.realcar.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(Long id) {
        super("O produto com id: " + id + "não foi encontrado.");
    }
}
