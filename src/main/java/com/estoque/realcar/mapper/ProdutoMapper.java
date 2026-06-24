package com.estoque.realcar.mapper;

import com.estoque.realcar.dto.request.ProdutoRequestDTO;
import com.estoque.realcar.dto.response.ProdutoResponseDTO;
import com.estoque.realcar.entities.Produto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    Produto toEntity(ProdutoRequestDTO dto);

    ProdutoResponseDTO toDto(Produto produto);

    List<Produto> toEntities(List<ProdutoRequestDTO> dtos);

    List<ProdutoResponseDTO> toDtos(List<Produto> produtos);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProdutoRequestDTO dto, @MappingTarget Produto produto);

}
