// ============================
// IMPORTAÇÃO EXCEL
// ============================

function setupExcelImportUI() {

    const arquivoExcel =
        document.getElementById('arquivoExcel');

    const importBtn =
        document.getElementById('importBtn');

    const confirmImportBtn =
        document.getElementById('confirmImportBtn');

    if (arquivoExcel) {

        arquivoExcel.addEventListener(
            'change',
            handleFileSelect
        );
    }

    // PREVIEW
    if (importBtn) {

        importBtn.addEventListener(
            'click',
            async (event) => {

                event.preventDefault();

                const file =
                    arquivoExcel.files[0];

                if (!file) {

                    showStatusMessage(
                        'Selecione um arquivo Excel',
                        'warning'
                    );

                    return;
                }

                await visualizarImportacao(file);
            }
        );
    }

    // IMPORTAÇÃO DEFINITIVA
    if (confirmImportBtn) {

        confirmImportBtn.addEventListener(
            'click',
            async () => {

                const file =
                    arquivoExcel.files[0];

                if (!file) {

                    showStatusMessage(
                        'Selecione um arquivo',
                        'warning'
                    );

                    return;
                }

                await realizarImportacao(file);
            }
        );
    }
}

// ============================
// VALIDAR ARQUIVO
// ============================

function handleFileSelect(event) {

    const file = event.target.files[0];

    if (!file) return;

    const extensoesValidas = ['.xlsx', '.xls'];

    const arquivoValido =
        extensoesValidas.some(ext =>
            file.name.toLowerCase().endsWith(ext)
        );

    if (!arquivoValido) {

        showStatusMessage(
            'Arquivo inválido. Use .xlsx ou .xls',
            'error'
        );

        event.target.value = '';

        return;
    }

    showStatusMessage(
        `Arquivo selecionado: ${file.name}`,
        'success'
    );
}

// ============================
// PREVIEW IMPORTAÇÃO
// ============================

async function visualizarImportacao(file) {

    const formData = new FormData();

    formData.append('file', file);

    try {

        showStatusMessage(
            'Processando arquivo...',
            'info'
        );

        const response = await fetch(
            `${API_URL}/importar/visualizar`,
            {
                method: 'POST',
                body: formData
            }
        );

        if (!response.ok) {

            throw new Error(
                `Erro HTTP ${response.status}`
            );
        }

        const data = await response.json();

        if (!data.produtos) {

            throw new Error(
                'Nenhum produto encontrado'
            );
        }

        displayPreviewTable(data.produtos);

        document
            .getElementById('previewContainer')
            .classList.remove('hidden');

        showStatusMessage(
            `${data.total} produto(s) encontrado(s)`,
            'success'
        );

    } catch (error) {

        console.error(error);

        showStatusMessage(
            error.message,
            'error'
        );
    }
}

// ============================
// IMPORTAÇÃO DEFINITIVA
// ============================

async function realizarImportacao(file) {

    const formData = new FormData();

    formData.append('file', file);

    try {

        showStatusMessage(
            'Importando produtos...',
            'info'
        );

        const response = await fetch(
            `${API_URL}/importar`,
            {
                method: 'POST',
                body: formData
            }
        );

        if (!response.ok) {

            throw new Error(
                `Erro HTTP ${response.status}`
            );
        }

        const data = await response.json();

        showStatusMessage(
            data.mensagem || 'Importação realizada!',
            'success'
        );

        document
            .getElementById('previewContainer')
            .classList.add('hidden');

        document
            .getElementById('arquivoExcel')
            .value = '';

        await loadProductsFromAPI();

    } catch (error) {

        console.error(error);

        showStatusMessage(
            error.message,
            'error'
        );
    }
}