package com.estoque.realcar.repository;

import com.estoque.realcar.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByCodigo(String codigo);

    Produto findByCodigo(String codigo);
}
