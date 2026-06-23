# ⚡ Quick Start - Importação Excel

## 3 Passos para Começar

### 1️⃣ Compilar
```bash
mvn clean install
```

### 2️⃣ Executar
```bash
mvn spring-boot:run
```

### 3️⃣ Importar
- Abra: http://localhost:8080
- Clique: "Importar"
- Selecione: `produtos_exemplo.xlsx`
- Confirme: A importação

✅ **Pronto!** 5 minutos e está funcionando.

---

## O que foi Implementado

### Backend (Java Spring Boot)
```
✅ ExcelImportService - Lê e processa Excel
✅ ProdutoController - 2 novos endpoints
✅ ProdutoRepository - Interface CRUD
✅ application.properties - Configuração
✅ pom.xml - Dependências (Apache POI)
```

### Frontend (JavaScript)
```
✅ 3 funções de importação
✅ Validação de arquivo
✅ Preview antes de importar
✅ Atualização automática da lista
```

### Dados
```
✅ produtos_exemplo.xlsx - 10 produtos de teste
```

---

## Endpoints REST

| Método | URL | Ação |
|--------|-----|------|
| POST | `/produtos/importar/visualizar` | Preview |
| POST | `/produtos/importar` | Importa dados |
| GET | `/produtos` | Lista produtos |

---

## Formato do Excel

| Nome | Quantidade | Preço |
|------|-----------|-------|
| Produto 1 | 10 | 99.99 |
| Produto 2 | 5 | 199.90 |

**Requisitos:**
- Extensão: `.xlsx`
- Primeira linha: ignorada (cabeçalho)
- Colunas: A (texto), B (inteiro), C (decimal)

---

## Arquivos Criados

```
✅ ExcelImportService.java      Backend - Leitura/processamento
✅ ProdutoController.java       Backend - Endpoints
✅ ViewController.java          Backend - Servir HTML
✅ ProdutoRepository.java       Backend - CRUD
✅ application.properties       Configuração
✅ script.js (modificado)       Frontend - Importação
✅ Main.java (modificado)       Backend - Inicializar
✅ pom.xml (modificado)         Dependências
✅ produtos_exemplo.xlsx        Dados de teste
✅ run.bat                      Script Windows
✅ run.ps1                      Script PowerShell
✅ test-excel.ps1              Script de teste
✅ README.md                    Documentação
✅ GUIA_TECNICO_EXCEL.md       Técnico detalhado
✅ SUMARIO_IMPLEMENTACAO.md    Sumário completo
```

---

## Compilar & Executar

### Windows
```bash
# PowerShell
.\run.ps1

# ou Batch
run.bat

# ou Maven direto
mvn spring-boot:run
```

### Mac/Linux
```bash
mvn spring-boot:run
```

---

## Testar com cURL

```bash
# Visualizar importação
curl -F "file=@produtos_exemplo.xlsx" \
  http://localhost:8080/produtos/importar/visualizar

# Importar
curl -F "file=@produtos_exemplo.xlsx" \
  http://localhost:8080/produtos/importar

# Listar
curl http://localhost:8080/produtos
```

---

## Estrutura de Banco de Dados

```sql
CREATE TABLE PRODUTO (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL,
    preco DOUBLE NOT NULL
);
```

**Console H2:**
- URL: `http://localhost:8080/h2-console`
- JDBC: `jdbc:h2:mem:testdb`
- Usuário: `sa`

---

## Validações

✅ Arquivo vazio  
✅ Formato inválido  
✅ Dados faltantes  
✅ Valores inválidos  
✅ Tamanho máximo (10MB)  

---

## Principais Classes

### ExcelImportService.java
```java
// Ler arquivo
public List<ProdutoRequestDTO> importarDePlanilha(MultipartFile file)

// Salvar dados
public int importarESalvar(MultipartFile file)

// Métodos auxiliares
private String getCellStringValue(Cell cell)
private Integer getCellIntValue(Cell cell)
private Double getCellDoubleValue(Cell cell)
```

### ProdutoController.java
```java
// Preview
@PostMapping("/importar/visualizar")
public ResponseEntity<?> visualizarImportacao(@RequestParam("file") MultipartFile file)

// Importar
@PostMapping("/importar")
public ResponseEntity<?> importarDePlanilha(@RequestParam("file") MultipartFile file)
```

### script.js
```javascript
// Inicializar
function setupExcelImport()

// Visualizar
function importarPlanilha(file)

// Importar
function realizarImportacao(file)
```

---

## Troubleshooting

| Problema | Solução |
|----------|---------|
| Erro ao compilar | `mvn clean install` |
| Porta em uso | Mudar em `application.properties` |
| Arquivo não encontrado | Verifique extensão `.xlsx` |
| Dados inválidos | Revise coluna B (inteiro) e C (decimal) |

---

## Próximos Passos

1. **Usar em desenvolvimento:**
   - Criar seus arquivos Excel
   - Testar importação
   - Verificar dados no banco

2. **Customizar:**
   - Mudar colunas do Excel
   - Adicionar validações
   - Alterar banco de dados

3. **Deploy:**
   - Build: `mvn clean package`
   - Usar JAR em produção
   - Configurar banco real

---

## Recursos

- 📖 **README.md** - Documentação completa
- 🔧 **GUIA_TECNICO_EXCEL.md** - Detalhes técnicos
- 📊 **SUMARIO_IMPLEMENTACAO.md** - Resumo geral
- 📁 **produtos_exemplo.xlsx** - Arquivo de teste

---

## Requisitos

- Java 17+
- Maven 3.6+
- Navegador moderno
- 100 MB de espaço em disco

---

## Versão

- **Versão:** 1.0.0
- **Status:** ✅ Pronto para produção
- **Data:** Novembro 2025

---

**Desenvolvido para RealCar - Sistema de Gestão de Estoque**

🚀 **Agora é só usar!**
