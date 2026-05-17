// ============================
// CONFIGURAÇÃO API
// ============================

const API_URL = 'http://localhost:8080/produtos';

// ============================
// DADOS
// ============================

let products = [];

// ============================
// ELEMENTOS DOM
// ============================

const productsTableBody = document.getElementById('productsTableBody');
const emptyState = document.getElementById('emptyState');

const searchInput = document.getElementById('searchInput');
const lowStockFilter = document.getElementById('lowStockFilter');
const sortSelect = document.getElementById('sortSelect');

const addProductBtn = document.getElementById('addProductBtn');

const productModal = document.getElementById('productModal');
const deleteModal = document.getElementById('deleteModal');

const productForm = document.getElementById('productForm');

const saveProductBtn = document.getElementById('saveProduct');

const cancelModalBtn = document.getElementById('cancelModal');
const closeModalBtn = document.getElementById('closeModal');

const confirmDeleteBtn = document.getElementById('confirmDelete');
const cancelDeleteBtn = document.getElementById('cancelDelete');
const closeDeleteModalBtn = document.getElementById('closeDeleteModal');

const productIdInput = document.getElementById('productId');
const productIdToDelete = document.getElementById('productIdToDelete');

const prevPageBtn = document.getElementById('prevPage');
const nextPageBtn = document.getElementById('nextPage');

const pageNumbersContainer = document.getElementById('pageNumbers');

const showingFrom = document.getElementById('showingFrom');
const showingTo = document.getElementById('showingTo');
const totalItems = document.getElementById('totalItems');

const totalProductsEl = document.getElementById('totalProducts');
const totalValueEl = document.getElementById('totalValue');
const inStockProductsEl = document.getElementById('inStockProducts');
const outOfStockProductsEl = document.getElementById('outOfStockProducts');

// ============================
// PAGINAÇÃO
// ============================

let currentPage = 1;
const itemsPerPage = 5;
let totalPages = 1;

// ============================
// INICIALIZAÇÃO
// ============================

document.addEventListener('DOMContentLoaded', () => {

    loadProductsFromAPI();

    setupExcelImportUI();

    setupEventListeners();
});

// ============================
// EVENTOS
// ============================

function setupEventListeners() {

    addProductBtn.addEventListener('click', openAddModal);

    saveProductBtn.addEventListener('click', saveProduct);

    cancelModalBtn.addEventListener('click', closeModal);

    closeModalBtn.addEventListener('click', closeModal);

    confirmDeleteBtn.addEventListener('click', deleteProduct);

    cancelDeleteBtn.addEventListener('click', closeDeleteModal);

    closeDeleteModalBtn.addEventListener('click', closeDeleteModal);

    searchInput.addEventListener('input', filterProducts);

    lowStockFilter.addEventListener('change', filterProducts);

    sortSelect.addEventListener('change', filterProducts);

    prevPageBtn.addEventListener('click', goToPrevPage);

    nextPageBtn.addEventListener('click', goToNextPage);
}

// ============================
// API
// ============================

async function loadProductsFromAPI() {

    try {

        const response = await fetch(API_URL);

        if (!response.ok) {

            throw new Error(
                `Erro HTTP: ${response.status}`
            );
        }

        const data = await response.json();

        products = data.map(product => ({

            id: product.id,

            description: product.nome,

            quantity: product.quantidade,

            price: product.preco
        }));

        updateStats();

        filterProducts();

    } catch (error) {

        console.error(
            'Erro ao carregar produtos:',
            error
        );

        showStatusMessage(
            'Erro ao carregar produtos da API',
            'error'
        );
    }
}

// ============================
// RENDER PRODUTOS
// ============================

function renderProducts(
    filteredProducts = products
) {

    productsTableBody.innerHTML = '';

    if (filteredProducts.length === 0) {

        emptyState.classList.remove('hidden');

        return;
    }

    emptyState.classList.add('hidden');

    const startIndex =
        (currentPage - 1) * itemsPerPage;

    const endIndex = Math.min(
        startIndex + itemsPerPage,
        filteredProducts.length
    );

    const paginatedProducts =
        filteredProducts.slice(
            startIndex,
            endIndex
        );

    showingFrom.textContent = startIndex + 1;

    showingTo.textContent = endIndex;

    totalItems.textContent =
        filteredProducts.length;

    paginatedProducts.forEach(product => {

        const row = document.createElement('tr');

        row.className = 'hover:bg-gray-50';

        const totalValue =
            product.quantity * product.price;

        const isLowStock =
            product.quantity > 0 &&
            product.quantity <= 5;

        const isOutOfStock =
            product.quantity === 0;

        row.innerHTML = `
            <td class="px-6 py-4">
                ${product.id}
            </td>

            <td class="px-6 py-4">
                ${product.description}
            </td>

            <td class="px-6 py-4">
                <span class="
                    px-2 py-1 rounded-full text-xs font-medium
                    ${
            isOutOfStock
                ? 'bg-red-100 text-red-800'
                : isLowStock
                    ? 'bg-yellow-100 text-yellow-800'
                    : 'bg-green-100 text-green-800'
        }
                ">
                    ${product.quantity}
                </span>
            </td>

            <td class="px-6 py-4">
                R$ ${product.price
            .toFixed(2)
            .replace('.', ',')}
            </td>

            <td class="px-6 py-4 font-semibold">
                R$ ${totalValue
            .toFixed(2)
            .replace('.', ',')}
            </td>

            <td class="px-6 py-4">

                <div class="flex gap-2">

                    <button
                        class="edit-btn text-blue-500"
                        data-id="${product.id}"
                    >
                        <i class="fas fa-edit"></i>
                    </button>

                    <button
                        class="delete-btn text-red-500"
                        data-id="${product.id}"
                    >
                        <i class="fas fa-trash"></i>
                    </button>

                </div>

            </td>
        `;

        productsTableBody.appendChild(row);
    });

    setupActionButtons();
}

// ============================
// BOTÕES DE AÇÃO
// ============================

function setupActionButtons() {

    document
        .querySelectorAll('.edit-btn')
        .forEach(btn => {

            btn.addEventListener('click', e => {

                const id = parseInt(
                    e.currentTarget.dataset.id
                );

                openEditModal(id);
            });
        });

    document
        .querySelectorAll('.delete-btn')
        .forEach(btn => {

            btn.addEventListener('click', e => {

                const id = parseInt(
                    e.currentTarget.dataset.id
                );

                openDeleteModal(id);
            });
        });
}

// ============================
// FILTRO
// ============================

function filterProducts() {

    const searchTerm =
        searchInput.value.toLowerCase();

    const sortBy = sortSelect.value;

    const lowStockOnly =
        lowStockFilter.checked;

    let filtered = products.filter(product => {

        return (
            product.description
                .toLowerCase()
                .includes(searchTerm)
            ||
            product.id
                .toString()
                .includes(searchTerm)
        );
    });

    if (lowStockOnly) {

        filtered = filtered.filter(product =>
            product.quantity > 0 &&
            product.quantity <= 5
        );
    }

    filtered.sort((a, b) => {

        switch (sortBy) {

            case 'name':
                return a.description.localeCompare(
                    b.description
                );

            case 'quantity':
                return a.quantity - b.quantity;

            case 'price':
                return a.price - b.price;

            default:
                return a.id - b.id;
        }
    });

    totalPages = Math.max(
        1,
        Math.ceil(filtered.length / itemsPerPage)
    );

    renderProducts(filtered);

    renderPagination();
}

// ============================
// PAGINAÇÃO
// ============================

function renderPagination() {

    pageNumbersContainer.innerHTML = '';

    prevPageBtn.disabled =
        currentPage === 1;

    nextPageBtn.disabled =
        currentPage === totalPages;

    for (let page = 1; page <= totalPages; page++) {

        const button =
            document.createElement('button');

        button.textContent = page;

        button.className =
            currentPage === page
                ? 'px-3 py-1 rounded bg-blue-500 text-white'
                : 'px-3 py-1 rounded border';

        button.addEventListener('click', () => {

            currentPage = page;

            filterProducts();
        });

        pageNumbersContainer.appendChild(button);
    }
}

function goToPrevPage() {

    if (currentPage > 1) {

        currentPage--;

        filterProducts();
    }
}

function goToNextPage() {

    if (currentPage < totalPages) {

        currentPage++;

        filterProducts();
    }
}

// ============================
// MODAIS
// ============================

function openAddModal() {

    productForm.reset();

    productIdInput.value = '';

    document.getElementById(
        'modalTitle'
    ).textContent = 'Adicionar Produto';

    productModal.classList.remove('hidden');
}

function openEditModal(productId) {

    const product = products.find(
        p => p.id === productId
    );

    if (!product) return;

    productIdInput.value = product.id;

    document.getElementById(
        'productDescription'
    ).value = product.description;

    document.getElementById(
        'productQuantity'
    ).value = product.quantity;

    document.getElementById(
        'productPrice'
    ).value = product.price;

    document.getElementById(
        'modalTitle'
    ).textContent = 'Editar Produto';

    productModal.classList.remove('hidden');
}

function closeModal() {

    productModal.classList.add('hidden');
}

function openDeleteModal(productId) {

    productIdToDelete.value = productId;

    deleteModal.classList.remove('hidden');
}

function closeDeleteModal() {

    deleteModal.classList.add('hidden');
}

// ============================
// CRUD
// ============================

async function saveProduct() {

    if (!productForm.checkValidity()) {

        productForm.reportValidity();

        return;
    }

    const id = productIdInput.value;

    const payload = {

        nome: document.getElementById(
            'productDescription'
        ).value,

        quantidade: parseInt(
            document.getElementById(
                'productQuantity'
            ).value
        ),

        preco: parseFloat(
            document.getElementById(
                'productPrice'
            ).value
        )
    };

    try {

        const response = await fetch(

            id
                ? `${API_URL}/${id}`
                : API_URL,

            {
                method: id ? 'PUT' : 'POST',

                headers: {
                    'Content-Type': 'application/json'
                },

                body: JSON.stringify(payload)
            }
        );

        if (!response.ok) {

            throw new Error(
                'Erro ao salvar produto'
            );
        }

        closeModal();

        await loadProductsFromAPI();

        showStatusMessage(
            'Produto salvo com sucesso!',
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

async function deleteProduct() {

    const productId = parseInt(
        productIdToDelete.value
    );

    try {

        const response = await fetch(
            `${API_URL}/${productId}`,
            {
                method: 'DELETE'
            }
        );

        if (!response.ok) {

            throw new Error(
                'Erro ao excluir produto'
            );
        }

        closeDeleteModal();

        await loadProductsFromAPI();

        showStatusMessage(
            'Produto excluído com sucesso!',
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
// ESTATÍSTICAS
// ============================

function updateStats() {

    const totalProducts = products.length;

    const totalValue = products.reduce(
        (sum, product) =>
            sum + (
                product.quantity *
                product.price
            ),
        0
    );

    const inStockProducts =
        products.filter(
            p => p.quantity > 0
        ).length;

    const outOfStockProducts =
        products.filter(
            p => p.quantity === 0
        ).length;

    totalProductsEl.textContent =
        totalProducts;

    totalValueEl.textContent =
        `R$ ${totalValue
            .toFixed(2)
            .replace('.', ',')}`;

    inStockProductsEl.textContent =
        inStockProducts;

    outOfStockProductsEl.textContent =
        outOfStockProducts;
}

// ============================
// IMPORTAÇÃO EXCEL
// ============================

function setupExcelImportUI() {

    const arquivoExcel =
        document.getElementById(
            'arquivoExcel'
        );

    const importBtn =
        document.getElementById(
            'importBtn'
        );

    const confirmImportBtn =
        document.getElementById(
            'confirmImportBtn'
        );

    if (arquivoExcel) {

        arquivoExcel.addEventListener(
            'change',
            handleFileSelect
        );
    }

    if (importBtn) {

        importBtn.addEventListener(
            'click',
            event => {

                event.preventDefault();

                const file =
                    arquivoExcel.files[0];

                if (!file) {

                    showStatusMessage(
                        'Selecione um arquivo',
                        'warning'
                    );

                    return;
                }

                visualizarImportacao(file);
            }
        );
    }

    if (confirmImportBtn) {

        confirmImportBtn.addEventListener(
            'click',
            () => {

                const file =
                    arquivoExcel.files[0];

                if (file) {

                    realizarImportacao(file);
                }
            }
        );
    }
}

function handleFileSelect(event) {

    const file = event.target.files[0];

    if (!file) return;

    const validExtensions =
        ['.xlsx', '.xls'];

    const isValid =
        validExtensions.some(ext =>
            file.name.endsWith(ext)
        );

    if (!isValid) {

        showStatusMessage(
            'Arquivo Excel inválido',
            'error'
        );

        event.target.value = '';

        return;
    }

    showStatusMessage(
        `Arquivo selecionado: ${file.name}`,
        'info'
    );
}

async function visualizarImportacao(file) {

    const formData = new FormData();

    formData.append('file', file);

    try {

        const response = await fetch(
            `${API_URL}/importar/visualizar`,
            {
                method: 'POST',
                body: formData
            }
        );

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.erro || 'Erro na importação'
            );
        }

        displayPreviewTable(data.produtos);

        document
            .getElementById(
                'previewContainer'
            )
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

async function realizarImportacao(file) {

    const formData = new FormData();

    formData.append('file', file);

    try {

        const response = await fetch(
            `${API_URL}/importar`,
            {
                method: 'POST',
                body: formData
            }
        );

        const data = await response.json();

        if (!response.ok) {

            throw new Error(
                data.erro || 'Erro ao importar'
            );
        }

        showStatusMessage(
            data.mensagem,
            'success'
        );

        document.getElementById(
            'previewContainer'
        ).classList.add('hidden');

        document.getElementById(
            'arquivoExcel'
        ).value = '';

        await loadProductsFromAPI();

    } catch (error) {

        console.error(error);

        showStatusMessage(
            error.message,
            'error'
        );
    }
}

// ============================
// PREVIEW TABELA
// ============================

function displayPreviewTable(produtos) {

    const table =
        document.getElementById(
            'previewTable'
        );

    const thead =
        table.querySelector('thead tr');

    const tbody =
        table.querySelector('tbody');

    thead.innerHTML = '';

    tbody.innerHTML = '';

    ['Nome', 'Quantidade', 'Preço']
        .forEach(text => {

            const th =
                document.createElement('th');

            th.textContent = text;

            th.className =
                'px-6 py-3 bg-gray-100';

            thead.appendChild(th);
        });

    produtos.forEach(produto => {

        const tr =
            document.createElement('tr');

        tr.innerHTML = `
            <td class="px-6 py-4">
                ${produto.nome}
            </td>

            <td class="px-6 py-4">
                ${produto.quantidade}
            </td>

            <td class="px-6 py-4">
                R$ ${parseFloat(produto.preco)
            .toFixed(2)
            .replace('.', ',')}
            </td>
        `;

        tbody.appendChild(tr);
    });
}

// ============================
// MENSAGENS
// ============================

function showStatusMessage(
    message,
    type = 'info'
) {

    const container =
        document.getElementById(
            'statusMessage'
        );

    if (!container) return;

    let colorClass =
        'bg-blue-100 text-blue-800';

    if (type === 'success') {
        colorClass =
            'bg-green-100 text-green-800';
    }

    if (type === 'error') {
        colorClass =
            'bg-red-100 text-red-800';
    }

    if (type === 'warning') {
        colorClass =
            'bg-yellow-100 text-yellow-800';
    }

    container.className =
        `${colorClass} p-4 rounded`;

    container.innerHTML = message;

    container.classList.remove('hidden');
}