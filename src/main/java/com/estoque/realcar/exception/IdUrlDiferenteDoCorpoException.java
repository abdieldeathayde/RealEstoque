package com.estoque.realcar.exception;

public class IdUrlDiferenteDoCorpoException extends RuntimeException {
    public IdUrlDiferenteDoCorpoException(Long idUrl, Long idCorpo) {
        super("O idUrl: " + idUrl + " é diferente do registro informado no corpo idCorpo: " + idCorpo + ".");
    }
}
