package com.estoque.realcar.service;

import com.estoque.realcar.dto.request.ProdutoRequestDTO;

import com.estoque.realcar.entities.Produto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelImportService {

    public List<ProdutoRequestDTO> importarDePlanilha(MultipartFile file)
            throws IOException {

        List<ProdutoRequestDTO> produtos = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);


        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            if (row == null) continue;

            Cell nomeCell = row.getCell(0); // coluna A
            Cell quantidadeCell = row.getCell(5); // coluna F
            Cell precoCell = row.getCell(7); // coluna H

            if (nomeCell == null) continue;

            String nome = getCellString(nomeCell);

            if (nome == null || nome.isBlank()) continue;

            Integer quantidade = getCellInteger(quantidadeCell);

            Double preco = getCellDouble(precoCell);

            ProdutoRequestDTO dto = new ProdutoRequestDTO();

            dto.setNome(nome.trim());
            dto.setQuantidade(quantidade != null ? quantidade : 0);
            dto.setPreco(preco != null ? preco : 0.0);

            produtos.add(dto);
        }

        workbook.close();

        return produtos;
    }

    private String getCellString(Cell cell) {

        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> null;
        };
    }

    private Integer getCellInteger(Cell cell) {

        if (cell == null) return 0;

        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }

        return 0;
    }

    private Double getCellDouble(Cell cell) {

        if (cell == null) return 0.0;

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }

        return 0.0;
    }

    public int importarESalvar(MultipartFile file)
            throws IOException {

        Repository repository = null;

        int total = 0;

        Workbook workbook =
                new XSSFWorkbook(file.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            if (row == null) continue;

            Cell nomeCell = row.getCell(0);
            Cell quantidadeCell = row.getCell(5);
            Cell precoCell = row.getCell(7);

            if (nomeCell == null) continue;

            String nome = nomeCell.toString().trim();

            if (nome.isBlank()) continue;

            int quantidade = 0;

            if (quantidadeCell != null &&
                    quantidadeCell.getCellType() == CellType.NUMERIC) {

                quantidade = (int)
                        quantidadeCell.getNumericCellValue();
            }

            double preco = 0.0;

            if (precoCell != null &&
                    precoCell.getCellType() == CellType.NUMERIC) {

                preco = precoCell.getNumericCellValue();
            }

            Produto produto = new Produto();

            produto.setNome(nome);
            produto.setQuantidade(quantidade);
            produto.setPreco(preco);

            repository.save(produto);

            total++;
        }

        workbook.close();

        return total;
    }
}
