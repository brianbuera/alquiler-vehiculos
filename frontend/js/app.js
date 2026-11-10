'use strict';

// AutoFlow · Frontend para los controllers Java suministrados.
// Todas las peticiones se hacen a /api y Nginx las redirige al servicio alquiler-api.
const API_BASE = '/api';
const $ = (selector) => document.querySelector(selector);
const esc = (value) => String(value ?? '').replace(/[&<>"']/g, (c) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' })[c]);
const currency = (amount) => new Intl.NumberFormat('es-AR', { style: 'currency', currency: 'ARS', maximumFractionDigits: 0 }).format(Number(amount) || 0);
const displayDate = (iso) => iso ? new Date(`${iso}T12:00:00`).toLocaleDateString('es-AR') : '—';
const count = (value) => (Array.isArray(value) ? value.length : 0);

const icons = {
  grid: '<rect x="3" y="3" width="7" height="7" rx="1.5"/><rect x="14" y="3" width="7" height="7" rx="1.5"/><rect x="3" y="14" width="7" height="7" rx="1.5"/><rect x="14" y="14" width="7" height="7" rx="1.5"/>',
  car: '<path d="M5 17h14l1.5-5-2.5-6H6L3.5 12 5 17Z"/><path d="M5 17v2m14-2v2M3.5 12h17M7 15h.01M17 15h.01"/>',
  users: '<circle cx="9" cy="8" r="3"/><path d="M3 20v-2a6 6 0 0 1 12 0v2M17 5a3 3 0 0 1 0 6m1 3a5 5 0 0 1 3 4v2"/>',
  calendar: '<rect x="3" y="5" width="18" height="16" rx="2"/><path d="M7 3v4m10-4v4M3 10h18"/>',
  plus: '<path d="M12 5v14M5 12h14"/>',
  search: '<circle cx="11" cy="11" r="7"/><path d="m16 16 4.5 4.5"/>',
  arrow: '<path d="M5 12h14m-6-6 6 6-6 6"/>',
  check: '<path d="m5 12 4 4L19 6"/>',
  clipboard: '<rect x="7" y="4" width="13" height="17" rx="2"/><path d="M9 4h7M4 9v9a2 2 0 0 0 2 2"/>',
  folder: '<path d="M3 7a2 2 0 0 1 2-2h4l2 2h8a2 2 0 0 1 2 2v10H3V7Z"/>',
  refresh: '<path d="M20 7v5h-5M4 17v-5h5"/><path d="M6 9a7 7 0 0 1 12-2l2 5M4 12l2 5a7 7 0 0 0 12-2"/>',
  sparkles: '<path d="m12 3 1.8 6.2L20 11l-6.2 1.8L12 19l-1.8-6.2L4 11l6.2-1.8L12 3ZM20 18l.6 1.4L22 20l-1.4.6L20 22l-.6-1.4L18 20l1.4-.6L20 18Z"/>',
  clock: '<circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/>',
};
const icon = (key) => `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">${icons[key] || icons.grid}</svg>`;

const state = {
  page: 'dashboard', clientes: [], vehiculos: [], reservas: [], filter: '', category: '',
  errors: [], connected: false, loading: true,
};
const pages = {
  dashboard: ['Dashboard', 'PANEL DE CONTROL', 'Dashboard', 'Todo lo que necesitás para gestionar tu flota.'],
  vehiculos: ['Vehículos', 'ADMINISTRACIÓN DE FLOTA', 'Vehículos', 'Administrá tu flota, las categorías y sus tarifas.'],
  clientes: ['Clientes', 'GESTIÓN DE PERSONAS', 'Clientes', 'Toda la información de tus clientes en un lugar.'],
  reservas: ['Reservas', 'GESTIÓN DE ALQUILERES', 'Reservas', 'Registrá alquileres y consultá su historial.'],
};

const api = {
  async request(path, method = 'GET', body) {
    let response;
    try {
      response = await fetch(API_BASE + path, {
        method,
        headers: { 'Accept': 'application/json', ...(body === undefined ? {} : { 'Content-Type': 'application/json' }) },
        ...(body === undefined ? {} : { body: JSON.stringify(body) }),
      });
    } catch (_e) {
      throw new Error('No se pudo conectar con Spring Boot. Comprobá que los contenedores estén ejecutándose.');
    }
    if (!response.ok) {
      let message = `Error HTTP ${response.status}`;
      try {
        const payload = await response.json();
        const detail = payload.message || payload.detail || payload.error;
        if (typeof detail === 'string' && detail.trim()) message += `: ${detail}`;
        if (typeof detail === 'string' && detail.trim() && response.status < 500) message = detail;
      } catch (_e) { /* A veces Spring devuelve texto vacío o HTML. */ }
      throw new Error(message);
    }
    if (response.status === 204) return null;
    const responseText = await response.text();
    if (!responseText) return null;
    try { return JSON.parse(responseText); }
    catch (_e) { throw new Error('La API devolvió una respuesta que no es JSON válido.'); }
  },
  get(path) { return this.request(path); },
  post(path, data) { return this.request(path, 'POST', data); },
  put(path, data) { return this.request(path, 'PUT', data); },
  delete(path) { return this.request(path, 'DELETE'); },
};

let toastTimer = null;
function notice(message, type = 'error') {
  const alert = $('#alert');
  alert.innerHTML = `<div class="notice ${type === 'success' ? 'ok' : type === 'warning' ? 'warn' : ''}">${esc(message)}</div>`;
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => { alert.innerHTML = ''; }, 7000);
}

async function reload() {
  const requests = ['/clientes', '/vehiculos', '/reservas'].map((path) => api.get(path));
  const [clientes, vehiculos, reservas] = await Promise.allSettled(requests);
  const entries = [['clientes', clientes], ['vehiculos', vehiculos], ['reservas', reservas]];
  state.errors = [];
  let successful = 0;
  for (const [key, result] of entries) {
    if (result.status === 'fulfilled' && Array.isArray(result.value)) {
      state[key] = result.value;
      successful++;
    } else {
      state[key] = [];
      state.errors.push(key);
    }
  }
  state.loading = false;
  state.connected = successful === 3;
  $('#side-status-dot').classList.toggle('offline', successful === 0);
  $('#side-status-dot').classList.toggle('loading', successful > 0 && successful < 3);
  $('#side-status-text').textContent = successful === 3 ? 'API conectada' : successful > 0 ? 'Conexión parcial' : 'API sin conexión';
  render();
  if (state.errors.length) notice(`No se pudieron cargar: ${state.errors.join(', ')}. Revisá la API y sus endpoints.`);
}

function go(page) {
  if (!(page in pages)) return;
  state.page = page;
  state.filter = '';
  state.category = '';
  render();
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

function render() {
  const [breadcrumb, eyebrow, title, subtitle] = pages[state.page];
  $('#breadcrumb').textContent = breadcrumb;
  $('#eyebrow').textContent = eyebrow;
  $('#page-title').textContent = title;
  $('#page-subtitle').textContent = subtitle;
  document.querySelectorAll('.nav-item').forEach((button) => {
    const active = button.dataset.page === state.page;
    button.classList.toggle('active', active);
    if (active) button.setAttribute('aria-current', 'page'); else button.removeAttribute('aria-current');
  });
  const addButtons = {
    vehiculos: `<button class="btn btn-primary" data-action="new-vehiculo">${icon('plus')} Nuevo vehículo</button>`,
    clientes: `<button class="btn btn-primary" data-action="new-cliente">${icon('plus')} Nuevo cliente</button>`,
    reservas: `<button class="btn btn-primary" data-action="new-reserva">${icon('plus')} Nueva reserva</button>`,
    dashboard: `<button class="btn btn-outline" data-action="reload">${icon('refresh')} Actualizar</button>`,
  };
  $('#header-actions').innerHTML = addButtons[state.page];
  $('#view').innerHTML = ({ dashboard: dashboardView, vehiculos: vehiculosView, clientes: clientesView, reservas: reservasView })[state.page]();
  bindFilters();
}

function stat(label, value, iconName, helper, tone = '') {
  return `<article class="stat"><div class="stat-head"><span>${label}</span><span class="stat-icon ${tone}">${icon(iconName)}</span></div><div class="stat-number">${value}</div><div class="stat-hint">${helper}</div></article>`;
}
function noResults(title, description, iconName = 'folder') {
  return `<div class="empty"><div class="empty-icon">${icon(iconName)}</div><strong>${esc(title)}</strong><p>${esc(description)}</p></div>`;
}
function dashboardView() {
  const disponibles = state.vehiculos.filter((v) => v.disponible).length;
  const ocupados = state.vehiculos.length - disponibles;
  const recent = [...state.reservas].sort((a, b) => Number(b.reservaId) - Number(a.reservaId)).slice(0, 5);
  return `<section class="stats" aria-label="Indicadores de la operación">
    ${stat('Vehículos totales', count(state.vehiculos), 'car', 'Unidades registradas')}
    ${stat('Disponibles', disponibles, 'check', 'Estado general de la flota', 'green')}
    ${stat('Clientes', count(state.clientes), 'users', 'Personas registradas', 'purple')}
    ${stat('Reservas', count(state.reservas), 'calendar', 'Alquileres registrados', 'orange')}
  </section>
  <section class="hero"><div class="hero-text"><div class="hero-tag">TU OPERACIÓN, EN UN SOLO LUGAR</div><h2>Gestioná tu flota con claridad.</h2><p>Administrá vehículos, organizá tus clientes y creá reservas consultando disponibilidad en tiempo real.</p><button class="btn" data-action="new-reserva">${icon('plus')} Crear nueva reserva</button></div><div class="hero-glyph" aria-hidden="true">A</div></section>
  <div class="dashboard-grid">
    <section class="panel"><div class="panel-top"><div><h3>Últimas reservas</h3><p>Los movimientos más recientes de tu sistema</p></div><button class="text-btn" data-page="reservas">Ver todas →</button></div>${reservaTable(recent, true)}</section>
    <section class="panel"><div class="panel-top"><div><h3>Estado de la flota</h3><p>Disponibilidad general de vehículos</p></div><span class="pill">En vivo</span></div>
      <div class="fleet-summary"><div><strong>${disponibles}</strong> <span>de ${state.vehiculos.length} vehículos</span></div><strong>${state.vehiculos.length ? Math.round(disponibles / state.vehiculos.length * 100) : 0}%</strong></div>
      <div class="fleet-bar"><div style="width:${state.vehiculos.length ? disponibles / state.vehiculos.length * 100 : 0}%"></div></div>
      <div class="legend"><div class="legend-item"><span><i class="legend-dot"></i> Disponibles</span><strong>${disponibles}</strong></div><div class="legend-item"><span><i class="legend-dot off"></i> No disponibles</span><strong>${ocupados}</strong></div></div>
      <p class="fleet-note">Este indicador refleja el campo «disponible» del vehículo. Para fechas específicas, usá la consulta de disponibilidad al crear una reserva.</p>
    </section>
  </div>`;
}
function filterInput(placeholder) {
  return `<div class="search-wrap">${icon('search')}<input class="search" id="search" type="search" autocomplete="off" placeholder="${esc(placeholder)}" value="${esc(state.filter)}" aria-label="Buscar"></div>`;
}
function vehiculosView() {
  return `<section class="panel page-panel"><div class="panel-top"><div><h3>Inventario de vehículos</h3><p>${count(state.vehiculos)} vehículos en el sistema</p></div><span class="pill dark">FLOTA</span></div>
    <div class="toolbar">${filterInput('Buscar por marca, modelo o dominio...')}<select class="select-filter" id="category" aria-label="Filtrar categoría"><option value="">Todas las categorías</option>${['ECONOMICO','PREMIUM','EJECUTIVO'].map((x) => `<option ${x === state.category ? 'selected' : ''}>${x}</option>`).join('')}</select></div>
    <div id="table-region">${vehiculoTable()}</div></section>`;
}
function vehiculoTable() {
  const needle = state.filter.toLocaleLowerCase('es-AR').trim();
  const rows = state.vehiculos.filter((v) => (!state.category || v.categoria === state.category) && [v.marca, v.modelo, v.dominio, v.anio, v.categoria].join(' ').toLocaleLowerCase('es-AR').includes(needle));
  if (!rows.length) return noResults('No hay vehículos para mostrar', 'Agregá un vehículo o modificá los filtros.', 'car');
  return `<div class="table-scroll"><table><thead><tr><th>Vehículo</th><th>Dominio / Año</th><th>Categoría</th><th>Capacidad</th><th>Tarifa diaria</th><th>Estado</th><th style="text-align:right">Acciones</th></tr></thead><tbody>${rows.map((v) => `<tr>
    <td><div class="cell-title">${esc(v.marca)} ${esc(v.modelo)}</div><div class="cell-sub">ID #${esc(v.id)}</div></td>
    <td><div class="cell-title">${esc(v.dominio)}</div><div class="cell-sub">${esc(v.anio)}</div></td>
    <td><span class="pill ${v.categoria === 'ECONOMICO' ? 'green' : v.categoria === 'EJECUTIVO' ? 'gold' : ''}">${esc(v.categoria)}</span></td>
    <td>${esc(v.capacidad)} personas</td><td class="cell-title money">${currency(v.precioDiario)}</td>
    <td><span class="pill ${v.disponible ? 'green' : 'red'}">${v.disponible ? 'Disponible' : 'No disponible'}</span></td>
    <td><div class="inline-actions"><button class="btn btn-subtle btn-sm" data-action="edit-vehiculo" data-id="${esc(v.id)}">Editar</button><button class="btn btn-danger btn-sm" data-action="delete-vehiculo" data-id="${esc(v.id)}">Eliminar</button></div></td></tr>`).join('')}</tbody></table></div>`;
}
function clientesView() {
  return `<section class="panel page-panel"><div class="panel-top"><div><h3>Directorio de clientes</h3><p>${count(state.clientes)} clientes registrados</p></div><span class="pill dark">CLIENTES</span></div>
    <div class="toolbar">${filterInput('Buscar por nombre o correo electrónico...')}</div><div id="table-region">${clienteTable()}</div></section>`;
}
function clienteTable() {
  const needle = state.filter.toLocaleLowerCase('es-AR').trim();
  const rows = state.clientes.filter((c) => [c.nombre, c.email, c.tipoCliente].join(' ').toLocaleLowerCase('es-AR').includes(needle));
  if (!rows.length) return noResults('No hay clientes para mostrar', 'Agregá tu primer cliente o probá otra búsqueda.', 'users');
  return `<div class="table-scroll"><table><thead><tr><th>Cliente</th><th>Correo electrónico</th><th>Tipo</th><th style="text-align:right">Acciones</th></tr></thead><tbody>${rows.map((c) => `<tr><td><div class="cell-title">${esc(c.nombre)}</div><div class="cell-sub">ID #${esc(c.id)}</div></td><td>${esc(c.email)}</td><td><span class="pill ${c.tipoCliente === 'VIP' ? 'gold' : c.tipoCliente === 'PREMIUM' ? 'green' : ''}">${esc(c.tipoCliente)}</span></td><td><div class="inline-actions"><button class="btn btn-subtle btn-sm" data-action="edit-cliente" data-id="${esc(c.id)}">Editar</button><button class="btn btn-danger btn-sm" data-action="delete-cliente" data-id="${esc(c.id)}">Eliminar</button></div></td></tr>`).join('')}</tbody></table></div>`;
}
function reservasView() {
  return `<section class="panel page-panel"><div class="panel-top"><div><h3>Historial de reservas</h3><p>${count(state.reservas)} reservas registradas</p></div><span class="pill dark">ALQUILERES</span></div>
    <div class="toolbar">${filterInput('Buscar por cliente, vehículo o ID...')}</div><div id="table-region">${reservaTable(filteredReservas())}</div></section>`;
}
function filteredReservas() {
  const needle = state.filter.toLocaleLowerCase('es-AR').trim();
  return state.reservas.filter((r) => [r.clienteNombre, r.vehiculo, r.reservaId].join(' ').toLocaleLowerCase('es-AR').includes(needle));
}
function reservaTable(rows, compact = false) {
  if (!rows.length) return noResults('No hay reservas para mostrar', 'Las reservas nuevas aparecerán en esta sección.', 'calendar');
  return `<div class="table-scroll"><table><thead><tr><th>Reserva</th><th>Cliente</th><th>Vehículo</th><th>Período</th><th>Total</th>${compact ? '' : '<th style="text-align:right">Acciones</th>'}</tr></thead><tbody>${rows.map((r) => `<tr>
    <td class="cell-title">#${esc(r.reservaId)}</td><td><div class="cell-title">${esc(r.clienteNombre)}</div>${compact ? '' : `<div class="cell-sub">${esc(r.tipoCliente)}</div>`}</td>
    <td>${esc(r.vehiculo)}</td><td><div class="cell-title">${displayDate(r.fechaInicio)}</div><div class="cell-sub">al ${displayDate(r.fechaFin)}</div></td>
    <td class="cell-title money">${currency(r.precioTotal)}</td>
    ${compact ? '' : `<td><div class="inline-actions"><button class="btn btn-subtle btn-sm" data-action="detail-reserva" data-id="${esc(r.reservaId)}">Ver detalle</button><button class="btn btn-danger btn-sm" data-action="delete-reserva" data-id="${esc(r.reservaId)}">Eliminar</button></div></td>`}</tr>`).join('')}</tbody></table></div>`;
}
function bindFilters() {
  const search = $('#search');
  if (search) search.addEventListener('input', (event) => {
    state.filter = event.target.value;
    updateTable(); // No reemplaza el input mientras el usuario escribe.
  });
  const category = $('#category');
  if (category) category.addEventListener('change', (event) => { state.category = event.target.value; updateTable(); });
}
function updateTable() {
  const region = $('#table-region');
  if (!region) return;
  region.innerHTML = state.page === 'vehiculos' ? vehiculoTable() : state.page === 'clientes' ? clienteTable() : reservaTable(filteredReservas());
}
function inputField(name, label, type = 'text', value = '', extra = '') {
  return `<div class="field"><label for="f-${name}">${esc(label)}</label><input class="form-control" id="f-${name}" name="${name}" type="${type}" value="${esc(value)}" ${extra} required></div>`;
}
function selectField(name, label, options, selected = '') {
  return `<div class="field"><label for="f-${name}">${esc(label)}</label><select class="form-control" id="f-${name}" name="${name}" required>${options.map((option) => {
    const [value, title] = Array.isArray(option) ? option : [option, option];
    return `<option value="${esc(value)}" ${String(value) === String(selected) ? 'selected' : ''}>${esc(title)}</option>`;
  }).join('')}</select></div>`;
}
let previousFocus = null;
function openModal({ title, fields, submit = 'Guardar', onSubmit = null }) {
  previousFocus = document.activeElement;
  $('#modal-title').textContent = title;
  $('#modal-body').innerHTML = fields;
  $('#form-error').classList.add('hidden');
  $('#form-error').textContent = '';
  const save = $('#modal-submit');
  save.classList.toggle('hidden', !onSubmit);
  save.textContent = submit;
  save.disabled = false;
  const overlay = $('#modal-backdrop');
  overlay.classList.remove('hidden');
  document.body.style.overflow = 'hidden';
  $('#modal-form').onsubmit = async (event) => {
    event.preventDefault();
    if (!onSubmit) return;
    save.disabled = true;
    $('#form-error').classList.add('hidden');
    try {
      await onSubmit(new FormData(event.currentTarget));
      closeModal();
      await reload();
      notice('Los cambios se guardaron correctamente.', 'success');
    } catch (error) {
      $('#form-error').textContent = error.message;
      $('#form-error').classList.remove('hidden');
    } finally {
      save.disabled = false;
    }
  };
  const focusable = $('#modal-body input, #modal-body select');
  (focusable || $('#modal-x')).focus();
}
function closeModal() {
  $('#modal-backdrop').classList.add('hidden');
  document.body.style.overflow = '';
  $('#modal-form').onsubmit = null;
  if (previousFocus?.isConnected) previousFocus.focus();
}

function clienteModal(id = null) {
  const current = id === null ? {} : state.clientes.find((c) => Number(c.id) === id);
  if (!current) return notice('No se encontró el cliente seleccionado.');
  openModal({
    title: id === null ? 'Nuevo cliente' : 'Editar cliente',
    fields: inputField('nombre', 'Nombre completo', 'text', current.nombre || '', 'minlength="2" maxlength="150"')
      + inputField('email', 'Correo electrónico', 'email', current.email || '', 'maxlength="200"')
      + selectField('tipoCliente', 'Tipo de cliente', ['ESTANDAR', 'PREMIUM', 'VIP'], current.tipoCliente || 'ESTANDAR'),
    onSubmit: async (form) => {
      const data = { nombre: form.get('nombre').trim(), email: form.get('email').trim(), tipoCliente: form.get('tipoCliente') };
      return id === null ? api.post('/clientes', data) : api.put(`/clientes/${id}`, data);
    },
  });
}
function vehiculoModal(id = null) {
  const current = id === null ? {} : state.vehiculos.find((v) => Number(v.id) === id);
  if (!current) return notice('No se encontró el vehículo seleccionado.');
  openModal({
    title: id === null ? 'Nuevo vehículo' : 'Editar vehículo',
    fields: `<div class="form-row">${inputField('marca', 'Marca', 'text', current.marca || '', 'maxlength="100"')}${inputField('modelo', 'Modelo', 'text', current.modelo || '', 'maxlength="100"')}</div>
      <div class="form-row">${inputField('anio', 'Año', 'text', current.anio || '', 'pattern="[0-9]{4}" inputmode="numeric" title="Ingresá un año de cuatro dígitos"')}${inputField('dominio', 'Dominio / Patente', 'text', current.dominio || '', 'maxlength="30"')}</div>
      <div class="form-row">${inputField('capacidad', 'Capacidad (4 a 8)', 'number', current.capacidad ?? 4, 'min="4" max="8" step="1"')}${inputField('precioDiario', 'Precio por día (ARS)', 'number', current.precioDiario ?? 0, 'min="0" step="0.01"')}</div>
      ${selectField('categoria', 'Categoría', ['ECONOMICO', 'PREMIUM', 'EJECUTIVO'], current.categoria || 'ECONOMICO')}
      <label class="check-field"><input type="checkbox" name="disponible" ${id === null || current.disponible ? 'checked' : ''}> Disponible para alquilar</label>`,
    onSubmit: async (form) => {
      const data = {
        marca: form.get('marca').trim(), modelo: form.get('modelo').trim(), anio: form.get('anio').trim(),
        dominio: form.get('dominio').trim(), capacidad: Number(form.get('capacidad')),
        categoria: form.get('categoria'), disponible: form.has('disponible'), precioDiario: Number(form.get('precioDiario')),
      };
      return id === null ? api.post('/vehiculos', data) : api.put(`/vehiculos/${id}`, data);
    },
  });
}
function reservaModal() {
  if (!state.clientes.length) { go('clientes'); return notice('Primero necesitás registrar un cliente para crear una reserva.', 'warning'); }
  if (!state.vehiculos.length) { go('vehiculos'); return notice('Primero necesitás registrar un vehículo para crear una reserva.', 'warning'); }
  openModal({
    title: 'Nueva reserva', submit: 'Crear reserva',
    fields: selectField('clienteId', 'Cliente', state.clientes.map((c) => [c.id, `${c.nombre} · ${c.tipoCliente}`]))
      + `<div class="form-row">${inputField('fechaInicio', 'Fecha de inicio', 'date')}${inputField('fechaFin', 'Fecha de fin', 'date')}</div>
      <button class="btn btn-outline" type="button" id="check-availability" style="margin-bottom:14px">${icon('search')} Consultar disponibilidad</button>
      <div class="availability-result" id="availability-message">Elegí las fechas y consultá los vehículos libres para ese período.</div>
      <div id="availability-select"></div>
      <label class="check-field"><input type="checkbox" name="telepaseIlimitado"> Incluir telepase ilimitado</label>`,
    onSubmit: async (form) => {
      const inicio = form.get('fechaInicio');
      const fin = form.get('fechaFin');
      if (fin < inicio) throw new Error('La fecha de finalización no puede ser anterior a la fecha de inicio.');
      const vehiculoId = form.get('vehiculoId');
      if (!vehiculoId) throw new Error('Primero consultá la disponibilidad y seleccioná un vehículo.');
      return api.post('/reservas', {
        clienteId: Number(form.get('clienteId')), vehiculoId: Number(vehiculoId),
        fechaInicio: inicio, fechaFin: fin, telepaseIlimitado: form.has('telepaseIlimitado'),
      });
    },
  });
  let searchNumber = 0;
  const reset = () => {
    searchNumber++;
    $('#availability-select').innerHTML = '';
    $('#availability-message').textContent = 'Las fechas cambiaron. Volvé a consultar disponibilidad.';
  };
  $('#f-fechaInicio').addEventListener('change', reset);
  $('#f-fechaFin').addEventListener('change', reset);
  $('#check-availability').addEventListener('click', async () => {
    const inicio = $('#f-fechaInicio').value;
    const fin = $('#f-fechaFin').value;
    if (!inicio || !fin) { $('#availability-message').textContent = 'Seleccioná las dos fechas primero.'; return; }
    if (fin < inicio) { $('#availability-message').textContent = 'La fecha de fin no puede ser anterior a la de inicio.'; return; }
    const requestNumber = ++searchNumber;
    $('#availability-select').innerHTML = '';
    $('#availability-message').textContent = 'Consultando disponibilidad...';
    $('#check-availability').disabled = true;
    try {
      const available = await api.get(`/vehiculos/buscar/por-fechas?inicio=${encodeURIComponent(inicio)}&fin=${encodeURIComponent(fin)}`);
      if (requestNumber !== searchNumber || $('#modal-backdrop').classList.contains('hidden')) return;
      if (!Array.isArray(available) || !available.length) {
        $('#availability-message').textContent = 'No hay vehículos disponibles para el período seleccionado.';
        return;
      }
      $('#availability-message').textContent = `${available.length} vehículo(s) disponible(s) para esas fechas.`;
      $('#availability-select').innerHTML = selectField('vehiculoId', 'Elegí un vehículo', available.map((v) => [v.id, `${v.marca} ${v.modelo} · ${currency(v.precioDiario)} por día`]));
    } catch (error) {
      if (requestNumber === searchNumber) $('#availability-message').textContent = error.message;
    } finally {
      if (!$('#modal-backdrop').classList.contains('hidden')) $('#check-availability').disabled = false;
    }
  });
}
function reservaDetail(id) {
  const r = state.reservas.find((item) => Number(item.reservaId) === id);
  if (!r) return notice('No se encontró la reserva seleccionada.');
  const data = [
    ['Cliente', r.clienteNombre], ['Tipo de cliente', r.tipoCliente], ['Vehículo', r.vehiculo],
    ['Fecha de inicio', displayDate(r.fechaInicio)], ['Fecha de fin', displayDate(r.fechaFin)],
    ['Precio base', currency(r.precioBase)], ['Cargo por telepase', currency(r.cargoTelepaseIlimitado)],
    ['Descuento aplicado', `${Number(r.porcentajeDescuento || 0).toLocaleString('es-AR', { maximumFractionDigits: 2 })}%`],
  ];
  openModal({
    title: `Detalle de reserva #${id}`,
    fields: data.map(([label, value]) => `<div class="detail-row"><span>${esc(label)}</span><strong>${esc(value)}</strong></div>`).join('')
      + `<div class="detail-row total"><span>Precio total</span><strong>${currency(r.precioTotal)}</strong></div>`,
    onSubmit: null,
  });
}
async function remove(kind, id) {
  const paths = { cliente: 'clientes', vehiculo: 'vehiculos', reserva: 'reservas' };
  const item = { cliente: 'cliente', vehiculo: 'vehículo', reserva: 'reserva' }[kind];
  if (!item || !Number.isFinite(id)) return;
  if (!window.confirm(`¿Querés eliminar ${item} #${id}? Esta acción no se puede deshacer.`)) return;
  try {
    await api.delete(`/${paths[kind]}/${id}`);
    await reload();
    notice(`${item[0].toUpperCase() + item.slice(1)} eliminado correctamente.`, 'success');
  } catch (error) { notice(error.message); }
}

document.addEventListener('click', (event) => {
  const page = event.target.closest('[data-page]');
  if (page) { go(page.dataset.page); return; }
  const control = event.target.closest('[data-action]');
  if (!control) return;
  const action = control.dataset.action;
  const id = Number(control.dataset.id);
  if (action === 'reload') { $('#view').innerHTML = '<div class="initial-loader"><div class="spinner"></div> Actualizando...</div>'; reload(); }
  if (action === 'new-cliente') clienteModal();
  if (action === 'new-vehiculo') vehiculoModal();
  if (action === 'new-reserva') reservaModal();
  if (action === 'edit-cliente') clienteModal(id);
  if (action === 'edit-vehiculo') vehiculoModal(id);
  if (action === 'detail-reserva') reservaDetail(id);
  if (action.startsWith('delete-')) remove(action.slice(7), id);
});
$('#modal-x').addEventListener('click', closeModal);
$('#modal-cancel').addEventListener('click', closeModal);
$('#modal-backdrop').addEventListener('mousedown', (event) => { if (event.target.id === 'modal-backdrop') closeModal(); });
document.addEventListener('keydown', (event) => { if (event.key === 'Escape' && !$('#modal-backdrop').classList.contains('hidden')) closeModal(); });
document.querySelectorAll('[data-icon]').forEach((node) => { node.innerHTML = icon(node.dataset.icon); });
$('#current-date').textContent = new Date().toLocaleDateString('es-AR', { day: 'numeric', month: 'long', year: 'numeric' });
reload();
