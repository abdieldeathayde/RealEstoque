package com.estoque.realcar.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estoque.realcar.dto.ProdutoDTO;
import com.estoque.realcar.entities.Produto;
import com.estoque.realcar.repository.ProdutoRepository;
import com.estoque.realcar.service.ExcelService;

@RestController
@RequestMapping("/api/planilha")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ProdutoRepository produtoRepository; // Certifique-se de ter essa linha injetada

    @PostMapping("/importar")
    public ResponseEntity<?> importarExcel(@RequestParam(value = "arquivo", required = false) MultipartFile fileArquivo,
                                           @RequestParam(value = "file", required = false) MultipartFile file) {
        
        MultipartFile arquivoFinal = fileArquivo != null ? fileArquivo : file;

        if (arquivoFinal == null || arquivoFinal.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo vazio.");
        }

        try {
            // 1. O service lê o Excel e te devolve uma lista (tipo genérico para evitar incompatibilidade)
            List<?> dtos = excelService.processarExcel(arquivoFinal);
            
            // 2. Converte a lista de DTOs para a Entidade do banco de dados (Produto)
            List<Produto> listaParaSalvar = new ArrayList<>();
            for (Object obj : dtos) {
                ProdutoDTO dto = (ProdutoDTO) obj;
                Produto produtoEntity = new Produto();
                produtoEntity.setCodigo(dto.getCodigo());
                produtoEntity.setNome(dto.getDescricao());      // DESCRIÇÃO -> nome
                produtoEntity.setDescricao(dto.getDescricao());
                produtoEntity.setQuantidade(dto.getQuantidade());
                produtoEntity.setPreco(dto.getValor());         // VALOR -> preco
                produtoEntity.setValor(dto.getValor());

                listaParaSalvar.add(produtoEntity);
            }

            // 3. ESTA LINHA SALVA NO BANCO DE DADOS EM LOTE (Massa)
            List<Produto> produtosSalvos = produtoRepository.saveAll(listaParaSalvar);

            // 4. Devolve para o front-end a lista já salva
            return ResponseEntity.ok(produtosSalvos); 
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao salvar no banco: " + e.getMessage());
        }
    }
}