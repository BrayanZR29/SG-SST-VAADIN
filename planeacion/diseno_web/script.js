// ===== NAVEGACIÓN =====
function navigateTo(page) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.getElementById(`page-${page}`).classList.add('active');
    
    document.querySelectorAll('.nav-item').forEach(item => item.classList.remove('active'));
    document.querySelector(`[data-page="${page}"]`).classList.add('active');
}

document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => {
        const page = item.dataset.page;
        navigateTo(page);
    });
});

// ===== LOGIN =====
function handleLogin(event) {
    event.preventDefault();
    document.getElementById('login-screen').classList.add('d-none');
    document.getElementById('main-screen').classList.remove('d-none');
    showToast('Bienvenido al sistema SST-Registro');
}

function handleLogout() {
    document.getElementById('main-screen').classList.add('d-none');
    document.getElementById('login-screen').classList.remove('d-none');
    showToast('Sesión cerrada correctamente');
}

// ===== REGISTRAR EVENTO =====
function handleRegistrar(event) {
    event.preventDefault();
    showToast('Evento registrado exitosamente. Código generado: ACC-013');
    navigateTo('consultar');
}

// ===== MODALES =====
let modalDetalle, modalInvestigacion;

document.addEventListener('DOMContentLoaded', () => {
    modalDetalle = new bootstrap.Modal(document.getElementById('modalDetalle'));
    modalInvestigacion = new bootstrap.Modal(document.getElementById('modalInvestigacion'));
});

function showDetalle(codigo) {
    document.getElementById('detalleCodigo').textContent = codigo;
    modalDetalle.show();
}

function showModal(tipo) {
    if (tipo === 'investigacion') {
        modalDetalle.hide();
        modalInvestigacion.show();
    } else if (tipo === 'usuario') {
        alert('Formulario de nuevo usuario (por implementar)');
    }
}

// ===== TOAST NOTIFICATION =====
function showToast(message) {
    const toast = document.getElementById('toast');
    const toastMessage = document.getElementById('toastMessage');
    toastMessage.textContent = message;
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
}

// ===== FILTROS DE TABLA =====
document.querySelectorAll('.filters-bar select, .filters-bar input').forEach(el => {
    el.addEventListener('change', () => {
        console.log('Filtro aplicado');
    });
});

// ===== BAR CHART HOVER EFFECT =====
document.querySelectorAll('.bar-fill').forEach(bar => {
    bar.addEventListener('mouseenter', function() {
        this.style.opacity = '0.8';
    });
    bar.addEventListener('mouseleave', function() {
        this.style.opacity = '1';
    });
});

// ===== INICIALIZACIÓN =====
console.log('SST-Registro - Prototipo cargado correctamente');
