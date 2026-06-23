#!/usr/bin/env pwsh
# Script para testar a importacao de Excel rapidamente

param(
    [string]$action = "help"
)

function Show-Help {
    Write-Host ""
    Write-Host "╔══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
    Write-Host "║     RealCar - Sistema de Gestao de Estoque              ║" -ForegroundColor Cyan
    Write-Host "║     Importador de Excel                                 ║" -ForegroundColor Cyan
    Write-Host "╚══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Uso: .\test-excel.ps1 [acao]" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Acoes disponíveis:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "  compile    - Compila o projeto" -ForegroundColor Green
    Write-Host "  run        - Executa a aplicacao" -ForegroundColor Green
    Write-Host "  build      - Compila e empacota" -ForegroundColor Green
    Write-Host "  test       - Executa testes" -ForegroundColor Green
    Write-Host "  clean      - Limpa artefatos" -ForegroundColor Green
    Write-Host "  help       - Mostra esta ajuda" -ForegroundColor Green
    Write-Host ""
    Write-Host "Exemplos:" -ForegroundColor Yellow
    Write-Host "  .\test-excel.ps1 compile" -ForegroundColor Gray
    Write-Host "  .\test-excel.ps1 run" -ForegroundColor Gray
    Write-Host "  .\test-excel.ps1 build" -ForegroundColor Gray
    Write-Host ""
}

function Compile-Project {
    Write-Host ""
    Write-Host "🔨 Compilando projeto..." -ForegroundColor Yellow
    mvn clean compile
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilacao concluida com sucesso!" -ForegroundColor Green
    } else {
        Write-Host "❌ Erro na compilacao" -ForegroundColor Red
    }
}

function Run-Application {
    Write-Host ""
    Write-Host "🚀 Iniciando aplicacao Spring Boot..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "A aplicacao estara disponivel em: http://localhost:8080" -ForegroundColor Cyan
    Write-Host "Pressione CTRL+C para parar" -ForegroundColor Gray
    Write-Host ""
    
    mvn spring-boot:run
}

function Build-Project {
    Write-Host ""
    Write-Host "📦 Compilando e empacotando..." -ForegroundColor Yellow
    mvn clean package -DskipTests
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ Build concluido!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Para executar o JAR:" -ForegroundColor Yellow
        Write-Host "  java -jar target/realcar-1.0-SNAPSHOT.jar" -ForegroundColor Gray
    }
}

function Test-Project {
    Write-Host ""
    Write-Host "🧪 Executando testes..." -ForegroundColor Yellow
    mvn test
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ Todos os testes passaram!" -ForegroundColor Green
    }
}

function Clean-Project {
    Write-Host ""
    Write-Host "🧹 Limpando projeto..." -ForegroundColor Yellow
    mvn clean
    Write-Host "✅ Limpeza concluida!" -ForegroundColor Green
}

function Check-Requirements {
    Write-Host ""
    Write-Host "🔍 Verificando requisitos..." -ForegroundColor Yellow
    
    # Verificar Java
    Write-Host "  Java..." -NoNewline
    java -version 2>&1 | Out-Null
    if ($LASTEXITCODE -eq 0) {
        Write-Host " ✅" -ForegroundColor Green
    } else {
        Write-Host " ❌ (nao instalado)" -ForegroundColor Red
        return $false
    }
    
    # Verificar Maven
    Write-Host "  Maven..." -NoNewline
    mvn --version 2>&1 | Out-Null
    if ($LASTEXITCODE -eq 0) {
        Write-Host " ✅" -ForegroundColor Green
    } else {
        Write-Host " ❌ (nao instalado)" -ForegroundColor Red
        return $false
    }
    
    # Verificar Excel de exemplo
    Write-Host "  Arquivo Excel..." -NoNewline
    if (Test-Path ".\produtos_exemplo.xlsx") {
        Write-Host " ✅" -ForegroundColor Green
    } else {
        Write-Host " ⚠️  (nao encontrado)" -ForegroundColor Yellow
    }
    
    Write-Host ""
    return $true
}

# Executar acao
switch ($action.ToLower()) {
    "compile" { Compile-Project }
    "run" { Run-Application }
    "build" { Build-Project }
    "test" { Test-Project }
    "clean" { Clean-Project }
    "check" { Check-Requirements }
    default { Show-Help }
}

Write-Host ""
