package com.estoque.realcar.controller;

import com.estoque.realcar.dto.request.ProdutoRequestDTO;
import com.estoque.realcar.dto.response.ProdutoResponseDTO;
import com.estoque.realcar.service.ExcelImportService;
import com.estoque.realcar.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final ExcelImportService excelImportService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Visualiza os produtos da planilha antes da importação
     */
    @PostMapping("/importar/visualizar")
    public ResponseEntity<?> visualizarImportacao(
            @RequestParam("file") MultipartFile file
    ) {
        try {

            List<ProdutoRequestDTO> produtos =
                    excelImportService.importarDePlanilha(file);

            return ResponseEntity.ok(
                    Map.of(
                            "total", produtos.size(),
                            "produtos", produtos,
                            "mensagem", "Produtos prontos para importar"
                    )
            );

        } catch (IOException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            Map.of(
                                    "erro",
                                    "Erro ao ler arquivo: " + e.getMessage()
                            )
                    );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            Map.of(
                                    "erro",
                                    e.getMessage()
                            )
                    );
        }
    }

    /**
     * Importa e salva produtos da planilha Excel
     */
    @PostMapping("/importar")
    public ResponseEntity<?> importarDePlanilha(
            @RequestParam("file") MultipartFile file
    ) {
        try {

            int totalImportado =
                    excelImportService.importarESalvar(file);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            Map.of(
                                    "sucesso", true,
                                    "totalImportado", totalImportado,
                                    "mensagem",
                                    totalImportado +
                                            " produto(s) importado(s) com sucesso"
                            )
                    );

        } catch (IOException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            Map.of(
                                    "sucesso", false,
                                    "erro",
                                    "Erro ao ler arquivo: " + e.getMessage()
                            )
                    );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            Map.of(
                                    "sucesso", false,
                                    "erro", e.getMessage()
                            )
                    );
        }
    }
}