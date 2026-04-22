# Propuesta Unificada — Esquema de Base de Datos

## Descripción General

Este documento describe el esquema de base de datos para un sistema de gestión de eventos, talleres, conferencias, usuarios y pagos.

---

## Tablas

### `usuarios`
Almacena la información principal de todos los usuarios del sistema.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| firstname | string | |
| lastname | string | |
| email | string | |
| password_hash | string | |
| telefono | string | |
| rol | string | |
| fecha_creacion | timestamp | |
| estado | string | |
| nivel_academico | string | |

---

### `roles`
Catálogo de roles disponibles en el sistema.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| nombre_rol | string | UK |
| descripcion | string | |

---

### `usuario_roles`
Relación muchos-a-muchos entre usuarios y roles.

| Campo | Tipo | Restricción |
|---|---|---|
| usuario_id | int | FK → usuarios |
| rol_id | int | FK → roles |

---

### `sesiones`
Registra las sesiones activas de los usuarios (tokens de autenticación).

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| token | string | |
| fecha_inicio | timestamp | |
| fecha_expiracion | timestamp | |

---

### `logs_usuario`
Bitácora de acciones realizadas por los usuarios.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| accion | string | |
| descripcion | string | |
| fecha | timestamp | |

---

### `organizaciones`
Entidades organizadoras de eventos.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| nombre | string | |
| tipo | string | |

---

### `eventos`
Eventos organizados por las organizaciones.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| organizacion_id | int | FK → organizaciones |
| nombre | string | |
| descripcion | text | |
| tipo | string | |
| estado | string | |
| fecha_inicio | timestamp | |
| fecha_fin | timestamp | |
| ubicacion | string | |
| capacidad_max | int | |

---

### `asignacion_usuario_evento`
Asignación de staff/personal a eventos con su rol específico.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| evento_id | int | FK → eventos |
| rol_id | int | FK → roles |
| especialidad_staff | string | |
| fecha_asignacion | timestamp | |
| asignado_por | int | FK → usuarios |

---

### `talleres`
Talleres que forman parte de un evento.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| evento_id | int | FK → eventos |
| titulo | string | |
| descripcion | text | |
| ponente | string | |
| duracion_min | int | |
| capacidad_max | int | |
| sala | string | |
| hora_inicio | timestamp | |
| hora_fin | timestamp | |
| estado | string | |

---

### `conferencias`
Conferencias que forman parte de un evento.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| evento_id | int | FK → eventos |
| titulo | string | |
| descripcion | text | |
| ponente | string | |
| duracion_min | int | |
| sala | string | |
| hora_inicio | timestamp | |
| hora_fin | timestamp | |

---

### `inscripciones_talleres`
Registro de inscripción de usuarios a talleres.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| taller_id | int | FK → talleres |
| estado | string | |
| inscrito_en | timestamp | |

---

### `asistencias`
Control de asistencia de usuarios a talleres.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| taller_id | int | FK → talleres |
| asistio | boolean | |
| registrado_en | timestamp | |

---

### `certificados`
Certificados generados para usuarios que completaron talleres.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| taller_id | int | FK → talleres |
| numero_certificado | string | UK |
| pdf_url | string | |
| emitido_en | timestamp | |
| enviado | boolean | |

---

### `pagos`
Registro de transacciones de pago realizadas por usuarios.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| referencia | string | UK |
| monto | decimal | |
| moneda | string | |
| estado | string | |
| metodo_pago | string | |
| pasarela | string | |
| pagado_en | timestamp | |
| creado_en | timestamp | |

---

### `recibos`
Comprobantes fiscales/recibos asociados a pagos.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| pago_id | int | FK → pagos |
| usuario_id | int | FK → usuarios |
| numero_recibo | string | UK |
| subtotal | decimal | |
| impuestos | decimal | |
| total | decimal | |
| pdf_url | string | |
| emitido_en | timestamp | |

---

### `inscripciones`
Registro de inscripción de usuarios a eventos (vincula pago).

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| evento_id | int | FK → eventos |
| pago_id | int | FK → pagos |
| estado | string | |
| inscrito_en | timestamp | |

---

### `credenciales`
Credenciales de acceso físico/digital emitidas a usuarios para eventos.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| numero_credencial | string | UK |
| estado | string | |
| emitida_en | timestamp | |
| expira_en | timestamp | |
| qr_codigo | string | |

---

### `perfiles`
Información de perfil extendido del usuario.

| Campo | Tipo | Restricción |
|---|---|---|
| id | int | PK |
| usuario_id | int | FK → usuarios |
| bio | text | |
| ciudad | string | |
| pais | string | |
| linkedin_url | string | |
| foto_url | string | |
| cv_file_url | string | |
| universidad | string | |
| grado_academico | string | |
| creado_en | timestamp | |

---

## Relaciones Clave

- Un **usuario** puede tener múltiples **roles** (vía `usuario_roles`).
- Una **organización** puede tener múltiples **eventos**.
- Un **evento** contiene **talleres** y **conferencias**.
- Un **usuario** se inscribe a un **evento** a través de **inscripciones** (requiere **pago**).
- Un **usuario** se inscribe a **talleres** individuales vía **inscripciones_talleres**.
- La **asistencia** a talleres genera **certificados**.
- Los **pagos** generan **recibos**.
- Los **usuarios** obtienen **credenciales** de acceso para los eventos.
