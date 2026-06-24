package com.estoque.realcar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estoque.realcar.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
