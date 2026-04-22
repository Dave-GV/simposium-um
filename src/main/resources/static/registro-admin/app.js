// =====================================================
//  Frontend - Registro de Administrador (Simposium UM)
// =====================================================

const API_BASE = '/api/admin';

// ---------- Helpers DOM ----------
const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => document.querySelectorAll(sel);

// ---------- Mensajes ----------
function mostrarMensaje(el, texto, tipo) {
  el.textContent = texto;
  el.className = 'mensaje ' + (tipo === 'ok' ? 'ok' : 'err');
}
function limpiarMensaje(el) {
  el.textContent = '';
  el.className = 'mensaje';
}

// ---------- Errores de validacion en campos ----------
function pintarErroresCampos(details) {
  $$('.field').forEach(f => f.classList.remove('invalid'));
  $$('[data-error-for]').forEach(s => (s.textContent = ''));
  if (!details) return;
  Object.entries(details).forEach(([campo, msg]) => {
    const input = document.getElementById(campo);
    if (input) {
      input.closest('.field').classList.add('invalid');
      const slot = document.querySelector(`[data-error-for="${campo}"]`);
      if (slot) slot.textContent = msg;
    }
  });
}

// ---------- Toggle mostrar/ocultar contraseña ----------
$$('.toggle-pass').forEach(btn => {
  btn.addEventListener('click', () => {
    const input = document.getElementById(btn.dataset.target);
    const eyeOn  = btn.querySelector('.eye-icon');
    const eyeOff = btn.querySelector('.eye-off-icon');
    if (input.type === 'password') {
      input.type = 'text';
      eyeOn.style.display  = 'none';
      eyeOff.style.display = '';
      btn.setAttribute('aria-label', 'Ocultar contraseña');
    } else {
      input.type = 'password';
      eyeOn.style.display  = '';
      eyeOff.style.display = 'none';
      btn.setAttribute('aria-label', 'Mostrar contraseña');
    }
  });
});

// ---------- Indicador de fortaleza de password ----------
const inputPwd = $('#contrasena');
const strengthEl = $('#strength');
inputPwd.addEventListener('input', () => {
  const v = inputPwd.value;
  let score = 0;
  if (v.length >= 8) score++;
  if (/[A-Za-z]/.test(v) && /\d/.test(v)) score++;
  if (v.length >= 12) score++;
  if (/[^A-Za-z0-9]/.test(v)) score++;
  strengthEl.className = 'strength s' + score;
});

// ---------- Submit registro ----------
const formRegistro = $('#form-registro');
const btnRegistrar = $('#btn-registrar');
const mensajeRegistro = $('#mensaje');

formRegistro.addEventListener('submit', async (e) => {
  e.preventDefault();
  limpiarMensaje(mensajeRegistro);
  pintarErroresCampos(null);

  const data = Object.fromEntries(new FormData(formRegistro).entries());

  // Validacion local rapida: confirmacion de contrasena
  if (data.contrasena !== data.confirmarContrasena) {
    pintarErroresCampos({ confirmarContrasena: 'Las contrasenas no coinciden' });
    mostrarMensaje(mensajeRegistro, 'Revisa los campos marcados.', 'err');
    return;
  }

  btnRegistrar.disabled = true;
  btnRegistrar.querySelector('span').textContent = 'Registrando…';

  try {
    const res = await fetch(`${API_BASE}/registro`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    const body = await res.json().catch(() => ({}));

    if (res.ok) {
      mostrarMensaje(
        mensajeRegistro,
        `Administrador "${body.nombreCompleto}" registrado correctamente.`,
        'ok'
      );
      formRegistro.reset();
      strengthEl.className = 'strength';
      cargarAdministradores();
    } else {
      pintarErroresCampos(body.details);
      mostrarMensaje(
        mensajeRegistro,
        body.message || 'No se pudo completar el registro.',
        'err'
      );
    }
  } catch (err) {
    mostrarMensaje(mensajeRegistro, 'Error de red al contactar el servidor.', 'err');
  } finally {
    btnRegistrar.disabled = false;
    btnRegistrar.querySelector('span').textContent = 'Registrar administrador';
  }
});

// ---------- Listar administradores ----------
const listaEl = $('#lista-admins');
const contadorEl = $('#contador-admins');

function inicialesDe(nombre) {
  if (!nombre) return '?';
  return nombre.trim().split(/\s+/).slice(0, 2)
    .map(p => p[0]).join('').toUpperCase();
}

function renderizarLista(admins) {
  if (!admins || admins.length === 0) {
    listaEl.innerHTML = '<div class="empty">Sin administradores registrados.</div>';
    contadorEl.textContent = '0 cuentas';
    return;
  }
  contadorEl.textContent =
    `${admins.length} ${admins.length === 1 ? 'cuenta registrada' : 'cuentas registradas'}`;

  listaEl.innerHTML = admins.map(a => {
    const badgeRol = a.rol === 'SUPER_ADMIN'
      ? '<span class="badge super">Super</span>'
      : '<span class="badge admin">Admin</span>';
    const badgeEstado = a.activo ? '' : '<span class="badge inactivo">Inactivo</span>';
    return `
      <div class="admin-card">
        <div class="avatar">${inicialesDe(a.nombreCompleto)}</div>
        <div class="info">
          <div class="nombre">${a.nombreCompleto} ${badgeRol} ${badgeEstado}</div>
          <div class="meta">@${a.nombreUsuario} · ${a.correoElectronico}</div>
        </div>
        <div class="card-actions">
          <button class="btn-ghost" data-edit="${a.idAdministrador}" title="Editar">✎</button>
          <button class="btn-danger" data-del="${a.idAdministrador}" title="Eliminar">✕</button>
        </div>
      </div>
    `;
  }).join('');

  // Listeners de acciones
  $$('[data-edit]').forEach(b =>
    b.addEventListener('click', () => abrirModalEditar(b.dataset.edit, admins))
  );
  $$('[data-del]').forEach(b =>
    b.addEventListener('click', () => eliminarAdmin(b.dataset.del))
  );
}

async function cargarAdministradores() {
  try {
    contadorEl.textContent = 'Cargando…';
    const res = await fetch(API_BASE);
    if (!res.ok) throw new Error('Error ' + res.status);
    const data = await res.json();
    renderizarLista(data);
  } catch (err) {
    listaEl.innerHTML =
      '<div class="empty">No se pudieron cargar los administradores.</div>';
    contadorEl.textContent = 'Error';
  }
}

$('#btn-recargar').addEventListener('click', cargarAdministradores);

// ---------- Eliminar ----------
async function eliminarAdmin(id) {
  if (!confirm('¿Eliminar este administrador? Esta acción no se puede deshacer.')) return;
  try {
    const res = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
    if (res.ok) {
      cargarAdministradores();
    } else {
      const body = await res.json().catch(() => ({}));
      alert(body.message || 'No se pudo eliminar.');
    }
  } catch {
    alert('Error de red al eliminar.');
  }
}

// ---------- Editar (modal) ----------
const modal = $('#modal-editar');
const formEditar = $('#form-editar');
const mensajeEditar = $('#mensaje-editar');

function abrirModalEditar(id, admins) {
  const a = admins.find(x => String(x.idAdministrador) === String(id));
  if (!a) return;
  $('#edit-id').value = a.idAdministrador;
  $('#edit-nombre').value = a.nombreCompleto || '';
  $('#edit-correo').value = a.correoElectronico || '';
  $('#edit-telefono').value = a.numeroTelefono || '';
  $('#edit-rol').value = a.rol || 'ADMIN';
  $('#edit-activo').checked = !!a.activo;
  limpiarMensaje(mensajeEditar);
  modal.classList.remove('hidden');
}

function cerrarModal() {
  modal.classList.add('hidden');
}
$('#modal-cerrar').addEventListener('click', cerrarModal);
$('#modal-cancelar').addEventListener('click', cerrarModal);
modal.addEventListener('click', (e) => { if (e.target === modal) cerrarModal(); });

formEditar.addEventListener('submit', async (e) => {
  e.preventDefault();
  const id = $('#edit-id').value;
  const payload = {
    nombreCompleto: $('#edit-nombre').value.trim() || null,
    correoElectronico: $('#edit-correo').value.trim() || null,
    numeroTelefono: $('#edit-telefono').value.trim() || null,
    activo: $('#edit-activo').checked,
  };
  try {
    // 1. Editar datos
    const resEdit = await fetch(`${API_BASE}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
    if (!resEdit.ok) {
      const body = await resEdit.json().catch(() => ({}));
      mostrarMensaje(mensajeEditar, body.message || 'No se pudo guardar.', 'err');
      return;
    }
    // 2. Asignar rol (independiente)
    const rolNuevo = $('#edit-rol').value;
    const resRol = await fetch(`${API_BASE}/${id}/rol`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ rol: rolNuevo }),
    });
    if (!resRol.ok) {
      const body = await resRol.json().catch(() => ({}));
      mostrarMensaje(mensajeEditar, body.message || 'No se pudo asignar el rol.', 'err');
      return;
    }
    mostrarMensaje(mensajeEditar, 'Cambios guardados.', 'ok');
    cargarAdministradores();
    setTimeout(cerrarModal, 800);
  } catch {
    mostrarMensaje(mensajeEditar, 'Error de red.', 'err');
  }
});

// ---------- Init ----------
cargarAdministradores();
