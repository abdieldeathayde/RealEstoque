package com.estoque.realcar.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.estoque.realcar.dto.request.ProdutoRequestDTO;
import com.estoque.realcar.entities.Produto;
import com.estoque.realcar.repository.ProdutoRepository;

@Service
public class ExcelImportService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoRequestDTO> importarDePlanilha(MultipartFile file)
            throws IOException {

        List<ProdutoRequestDTO> produtos = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell codigoCell = row.getCell(0);     // Coluna Código
                Cell nomeCell = row.getCell(1);       // Coluna DESCRIÇÃO
                Cell quantidadeCell = row.getCell(2); // Coluna Quantidade
                Cell precoCell = row.getCell(3);      // Coluna VALOR

                if (codigoCell == null && nomeCell == null) continue;

                String codigo = getCellString(codigoCell);
                String nome = getCellString(nomeCell);
                if (nome == null || nome.isBlank()) continue;

                Integer quantidade = converterParaInteiro(getCellString(quantidadeCell));
                Double preco = converterParaDouble(getCellString(precoCell));

                ProdutoRequestDTO dto = new ProdutoRequestDTO();
                dto.setCodigo(codigo);
                dto.setNome(nome.trim());
                dto.setQuantidade(quantidade);
                dto.setPreco(preco != null ? preco : 0.0);

                produtos.add(dto);
            }
        }
        return produtos;
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";
        
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double valor = cell.getNumericCellValue();
                if (valor == (long) valor) {
                    yield String.valueOf((long) valor);
                }
                yield String.valueOf(valor);
            }
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue();
                } catch (Exception e) {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private Integer converterParaInteiro(String texto) {
        try {
            if (texto == null || texto.isBlank()) return 0;
            // Remove decimais se o Excel exportar como "10.0"
            if (texto.contains(".")) {
                texto = texto.split("\\.")[0];
            }
            return Integer.parseInt(texto.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private Double converterParaDouble(String texto) {
        try {
            if (texto == null || texto.isBlank()) return 0.0;
            // Limpa formatação comum (R$, espaços, separador de milhar)
            String limpo = texto.replaceAll("[R$\\s]", "")
                                .replace(".", "")
                                .replace(",", ".");
            return Double.parseDouble(limpo);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public int importarESalvar(MultipartFile file)
            throws IOException {
        int total = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {
            
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell codigoCell = row.getCell(0);
                Cell nomeCell = row.getCell(1);
                Cell quantidadeCell = row.getCell(2);
                Cell precoCell = row.getCell(3);

                String codigo = getCellString(codigoCell);
                String nome = getCellString(nomeCell);
                if (nome == null || nome.isBlank()) continue;

                int quantidade = converterParaInteiro(getCellString(quantidadeCell));
                double preco = converterParaDouble(getCellString(precoCell));

                Produto produto = new Produto();
                produto.setCodigo(codigo);
                produto.setNome(nome);
                produto.setDescricao(nome); // Preenche ambos para garantir exibição
                produto.setQuantidade(quantidade);
                produto.setPreco(preco);
                produto.setValor(preco);   // Preenche ambos para garantir exibição

                produtoRepository.save(produto);
                total++;
            }
        }
        return total;
    }
}
