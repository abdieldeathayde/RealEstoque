╔══════════════════════════════════════════════════════════════════════════════╗
║                  SISTEMA DE IMPORTAÇÃO EXCEL - FLUXO VISUAL                  ║
╚══════════════════════════════════════════════════════════════════════════════╝

┌─────────────────────────────────────────────────────────────────────────────┐
│                        INTERFACE DO USUÁRIO (HTML)                          │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │ 📁 Importar Arquivo XLSX           🔽 (Toggle Expand)               │   │
│  │ Faça upload de um arquivo .xlsx com seus produtos                   │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  [Selecionar arquivo]        [🔄 Importar]                                 │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │ Preview dos Produtos                                                 │   │
│  ├──────────────────────────────────────────────────────────────────────┤   │
│  │ Nome              │ Quantidade    │ Preço                            │   │
│  ├──────────────────────────────────────────────────────────────────────┤   │
│  │ Notebook Dell     │ 15            │ R$ 4.299,99                      │   │
│  │ Mouse Logitech    │ 42            │ R$ 129,90                        │   │
│  │ Teclado RGB       │ 8             │ R$ 349,90                        │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  [✅ Confirmar Importação]     [❌ Cancelar]                                │
│                                                                              │
│  ✅ Importação Concluída! 3 produto(s) importado(s) com sucesso            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────────────────────┐
│                    FLUXO DE PROCESSAMENTO (JAVASCRIPT)                      │
│                                                                              │
│  setupExcelImportUI()                                                       │
│  ├─ Configura listeners                                                     │
│  ├─ Toggle expandir/retrair                                                │
│  └─ Inicializa elementos                                                    │
│                    ↓                                                         │
│  handleFileSelect()                                                         │
│  ├─ Valida extensão (.xlsx, .xls)   ✓                                      │
│  ├─ Valida tamanho (max 10MB)        ✓                                      │
│  └─ Mostra mensagem de status                                               │
│                    ↓                                                         │
│  visualizarImportacao()                                                     │
│  ├─ Envia FormData para backend                                             │
│  ├─ Chama /importar/visualizar                                              │
│  ├─ Recebe lista de produtos                                                │
│  └─ Exibe tabela de preview                                                 │
│                    ↓                                                         │
│  displayPreviewTable()                                                      │
│  ├─ Cria cabeçalho profissional                                             │
│  ├─ Adiciona linhas com dados                                               │
│  ├─ Formata preços em reais                                                │
│  └─ Alterna cores (striped)                                                 │
│                    ↓                                                         │
│  showStatusMessage()                                                        │
│  ├─ Mostra número de produtos encontrados                                   │
│  ├─ Mensagem: "Revise os dados e confirme"                                  │
│  └─ Ativa botões de ação                                                    │
│                    ↓                                                         │
│  realizarImportacao()                                                       │
│  ├─ Envia para /importar                                                    │
│  ├─ Desabilita botão durante processamento                                  │
│  ├─ Recebe resposta do servidor                                             │
│  ├─ Exibe mensagem de sucesso/erro                                          │
│  ├─ Limpa formulário                                                        │
│  └─ Atualiza lista de produtos                                              │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────────────────────┐
│                     COMUNICAÇÃO COM BACKEND (REST API)                      │
│                                                                              │
│  ┌─────────────────────────────────┐  ┌──────────────────────────────────┐ │
│  │  Frontend (JavaScript)          │  │  Backend (Spring Boot)           │ │
│  │                                 │  │                                  │ │
│  │  POST /visualizar               │  │  ExcelImportService              │ │
│  │  ├─ file: arquivo.xlsx          │──→ importarDePlanilha()           │ │
│  │  └─ Retorna: lista de DTOs ←────┤  ├─ Lê arquivo XLS/XLSX           │ │
│  │                                 │  ├─ Extrai dados das células      │ │
│  │                                 │  ├─ Valida cada linha             │ │
│  │  Response 200 OK                │  ├─ Converte tipos automaticamente│ │
│  │  {                              │  └─ Retorna JSON com dados         │ │
│  │    "total": 3,                  │                                    │ │
│  │    "produtos": [                │  ProdutoService                    │ │
│  │      {                          │  ├─ salvar()                       │ │
│  │        "nome": "Notebook",      │  └─ listarTodos()                  │ │
│  │        "quantidade": 15,        │                                    │ │
│  │        "preco": 4299.99         │  ProdutoRepository                 │ │
│  │      },                         │  └─ JpaRepository<Produto, Long>   │ │
│  │      ...                        │                                    │ │
│  │    ]                            │  H2 Database                       │ │
│  │  }                              │  └─ Tabela PRODUTO                │ │
│  │                                 │                                    │ │
│  │  POST /importar                 │                                    │ │
│  │  ├─ file: arquivo.xlsx          │──→ importarESalvar()              │ │
│  │  └─ Retorna: {sucesso: true} ←──┤  ├─ Processa arquivo              │ │
│  │                                 │  ├─ Salva produtos no BD          │ │
│  │                                 │  ├─ Trata erros                   │ │
│  │  Response 201 CREATED           │  └─ Retorna contagem de salvos    │ │
│  │  {                              │                                    │ │
│  │    "sucesso": true,             │                                    │ │
│  │    "totalImportado": 3,         │                                    │ │
│  │    "mensagem": "..."            │                                    │ │
│  │  }                              │                                    │ │
│  │                                 │                                    │ │
│  └─────────────────────────────────┘  └──────────────────────────────────┘ │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────────────────────┐
│                        SISTEMA DE MENSAGENS DE STATUS                       │
│                                                                              │
│  INFO (Azul)                                                                │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ ℹ️  Arquivo selecionado: produtos.xlsx                              │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  SUCCESS (Verde)                                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ ✅ 3 produto(s) encontrado(s)                                       │   │
│  │    Revise os dados abaixo e confirme a importação                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ERROR (Vermelho)                                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ ⚠️  Erro: Arquivo deve ser um Excel (.xlsx ou .xls)                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  WARNING (Amarelo)                                                          │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ ⚡ Nenhum produto encontrado no arquivo                             │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘


┌─────────────────────────────────────────────────────────────────────────────┐
│                       VALIDAÇÕES IMPLEMENTADAS                              │
│                                                                              │
│  Frontend:                          Backend:                                │
│  ├─ Extensão do arquivo             ├─ Arquivo vazio                      │
│  ├─ Tamanho máximo (10MB)           ├─ Formato inválido                   │
│  ├─ Arquivo selecionado             ├─ Dados faltantes (nome, qtd, preço) │
│  └─ Mensagem ao usuário             ├─ Valores numéricos inválidos        │
│                                     ├─ Quantidade negativa                │
│                                     ├─ Preço negativo                     │
│                                     └─ Conversão de tipos automática      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════

✨ DESTAQUES DA IMPLEMENTAÇÃO:

  🎨 Interface Profissional
     • Design moderno com gradientes
     • Ícones Font Awesome
     • Animações suaves
     • Responsiva (mobile, tablet, desktop)

  📊 Preview Visual
     • Tabela formatada
     • Preços em reais
     • Alternância de cores (striped)
     • Hover effects

  🔐 Validações Robustas
     • Frontend: Tipo e tamanho de arquivo
     • Backend: Dados e tipos
     • Mensagens de erro claras
     • Recovery automático

  🚀 UX Otimizada
     • Toggle expandir/retrair
     • Confirmação antes de salvar
     • Feedback em tempo real
     • Auto-atualização da lista

═══════════════════════════════════════════════════════════════════════════════

                         ✅ PRONTO PARA USAR!

═══════════════════════════════════════════════════════════════════════════════
