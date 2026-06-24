package com.estoque.realcar.service;


import com.estoque.realcar.dto.request.ProdutoRequestDTO;
import com.estoque.realcar.dto.response.ProdutoResponseDTO;
import com.estoque.realcar.entities.Produto;
import com.estoque.realcar.mapper.ProdutoMapper;
import com.estoque.realcar.repository.ProdutoRepository;
import com.estoque.realcar.service.validator.ProdutoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final ProdutoValidator produtoValidator;

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoMapper.toDtos(produtoRepository.findAll());
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoValidator.buscaProdutoOuLancaException(id);
        return produtoMapper.toDto(produto);
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {
        produtoValidator.verificaSeProdutoJaExisteOuLancaException(dto.getCodigo());
        Produto produtoSalvo = produtoRepository.save(produtoMapper.toEntity(dto));
        return produtoMapper.toDto(produtoSalvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoValidator.buscaProdutoOuLancaException(id);
        produtoValidator.verificaSeIdUrlEIgualIdCorpo(id, dto.getCodigo());
        produtoMapper.updateEntity(dto, produto);
        return produtoMapper.toDto(produto);
    }

    @Transactional
    public void deletar(Long id) {
        produtoRepository.delete(produtoValidator.buscaProdutoOuLancaException(id));
    }

}
