package com.estoque.realcar.service.validator;

import com.estoque.realcar.entities.Produto;
import com.estoque.realcar.exception.IdUrlDiferenteDoCorpoException;
import com.estoque.realcar.exception.ProdutoJaExisteException;
import com.estoque.realcar.exception.ProdutoNaoEncontradoException;
import com.estoque.realcar.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProdutoValidator {

    private final ProdutoRepository produtoRepository;

    public Produto buscaProdutoOuLancaException(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    public void verificaSeProdutoJaExisteOuLancaException(String codigo) {
        if (produtoRepository.existsByCodigo(codigo)) {
            throw new ProdutoJaExisteException(codigo);
        }
    }

    public void verificaSeIdUrlEIgualIdCorpo(Long idUrl, String codigoCorpo) {
        Produto produto = produtoRepository.findByCodigo(codigoCorpo);
        if (!produto.getId().equals(idUrl)) {
            throw new IdUrlDiferenteDoCorpoException(idUrl, produto.getId());
        }
    }

}
