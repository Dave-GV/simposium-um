# Documentación técnica - Módulo Registro de Administrador

## 1. Objetivo del módulo

Proveer un CRUD completo para gestionar administradores del sistema Simposium UM, incluyendo:

- Registrar nuevos administradores con validación de datos.
- Listar, editar y eliminar administradores.
- Asignar roles (ADMIN / SUPER_ADMIN).
- Interfaz web funcional para operar el módulo desde el navegador.

---

## 2. Endpoints REST

Base: `/api/admin`

| Método | Ruta | Descripción | Auth requerida |
|--------|------|-------------|----------------|
| POST | `/api/admin/registro` | Registrar nuevo administrador | No (público) |
| GET | `/api/admin` | Listar todos los administradores | No (público) |
| GET | `/api/admin/{id}` | Obtener un administrador por ID | No (público) |
| PUT | `/api/admin/{id}` | Editar datos del administrador | No (público) |
| PATCH | `/api/admin/{id}/rol` | Asignar/cambiar rol | No (público) |
| DELETE | `/api/admin/{id}` | Eliminar administrador | No (público) |

> **Nota para el equipo:** Las rutas están actualmente abiertas para facilitar el desarrollo. En producción deberán protegerse con JWT y rol SUPER_ADMIN.

---

## 3. Contrato de datos

### POST /api/admin/registro — Request Body

```json
{
  "nombreCompleto": "Juan Pérez García",
  "correoElectronico": "juan@simposium.com",
  "nombreUsuario": "juan.perez",
  "contrasena": "MiPass123",
  "confirmarContrasena": "MiPass123",
  "numeroTelefono": "+52 55 1234 5678",
  "rolAdministrador": "ADMIN"
}
```

Valores válidos para `rolAdministrador`: `ADMIN`, `SUPER_ADMIN`.

`numeroTelefono` es opcional.

### Respuesta exitosa — 201 Created

```json
{
  "idAdministrador": 1,
  "nombreCompleto": "Juan Pérez García",
  "nombreUsuario": "juan.perez",
  "correoElectronico": "juan@simposium.com",
  "numeroTelefono": "+52 55 1234 5678",
  "rol": "ADMIN",
  "activo": true,
  "fechaRegistro": "2026-04-22T14:40:37-06:00"
}
```

> La contraseña **nunca** se devuelve en la respuesta. Se almacena como hash BCrypt.

### Códigos de error

| Código HTTP | Code interno | Causa |
|-------------|--------------|-------|
| 400 | `ADMIN_VALIDATION_ERROR` | Campo inválido o faltante |
| 400 | `ADMIN_PASSWORD_MISMATCH` | Contraseña y confirmación no coinciden |
| 404 | `ADMIN_NOT_FOUND` | ID no existe |
| 409 | `ADMIN_ALREADY_EXISTS` | Correo o nombre de usuario duplicado |

---

## 4. Arquitectura del módulo

```
com.example.simposium.admin/
├── entity/
│   ├── Administrador.java        ← Entidad JPA → tabla `administrador`
│   └── RolAdmin.java             ← Enum: ADMIN, SUPER_ADMIN
├── repository/
│   └── AdministradorRepository.java  ← JpaRepository con búsqueda por correo/usuario
├── dto/
│   ├── RegistroAdministradorRequest.java   ← Input registro
│   ├── ActualizarAdministradorRequest.java ← Input edición
│   ├── AsignarRolRequest.java              ← Input cambio de rol
│   └── AdministradorResponse.java          ← Output (sin contraseña)
├── service/
│   ├── IRegistroAdministrador.java ← Interfaz del módulo
│   ├── AdministradorService.java   ← Implementación
│   └── ValidadorRegistro.java      ← Validador de correo, contraseña y teléfono
└── controller/
    ├── AdministradorController.java        ← Endpoints REST
    └── AdministradorExceptionHandler.java  ← Manejo de errores en JSON
```

---

## 5. Tabla en base de datos

La tabla se llama `administrador` y Hibernate la crea automáticamente con `ddl-auto=update`.

| Columna | Tipo | Restricción |
|---------|------|-------------|
| id_administrador | BIGINT | PK, autoincrement |
| nombre_completo | VARCHAR(150) | NOT NULL |
| nombre_usuario | VARCHAR(60) | NOT NULL, UNIQUE |
| correo_electronico | VARCHAR(150) | NOT NULL, UNIQUE |
| contrasena_hash | VARCHAR(255) | NOT NULL (BCrypt) |
| numero_telefono | VARCHAR(20) | nullable |
| rol | VARCHAR(20) | NOT NULL, check: ADMIN/SUPER_ADMIN |
| activo | BOOLEAN | NOT NULL, default true |
| fecha_registro | TIMESTAMPTZ | NOT NULL |

---

## 6. Frontend

Ruta: `http://localhost:8080/registro-admin/`

Archivos en `src/main/resources/static/registro-admin/`:
- `index.html` — estructura de la vista
- `styles.css` — estilos modernos con gradientes y validación visual
- `app.js` — lógica: formulario de registro, lista en tiempo real, modal de edición, eliminación

Funcionalidades:
- Formulario con validación en tiempo real y confirmación de contraseña.
- Indicador visual de fortaleza de contraseña.
- Lista de administradores con badges de rol y estado.
- Modal para editar datos y cambiar rol.
- Botón eliminar con confirmación.

---

## 7. Configuración de entorno

### ⚠️ Problema conocido con Supabase (pendiente del equipo de BD)

La URL configurada en `application.properties`:
```
spring.datasource.url=jdbc:postgresql://db.kkrrsvbcrcvitvvxregb.supabase.co:5432/postgres
```

Este host **solo resuelve a IPv6**. Redes sin IPv6 enrutado a internet no pueden conectarse.

**Solución para el equipo de base de datos:**
1. Ir a Supabase → proyecto → **Connect → Connection pooler (Transaction mode)**
2. Copiar la URL que usa puerto **6543** (tiene IPv4)
3. Reemplazar `spring.datasource.url` en `application.properties` con esa URL

Ejemplo de cómo quedaría:
```properties
spring.datasource.url=jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:6543/postgres?pgbouncer=true
```

### Correr en modo local (sin Supabase)

Para desarrollar sin depender de la BD remota, usar el perfil `local`:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

Este perfil usa H2 en memoria. Los datos se pierden al reiniciar el servidor.
El archivo de configuración del perfil es `src/main/resources/application-local.properties`.

---

## 8. Próximos pasos sugeridos

1. **Proteger los endpoints** con JWT + verificación de rol SUPER_ADMIN.
2. **Envío de correo de verificación** al registrar (diagrama de secuencias contempla este paso).
3. **Integrar con el módulo de login** para que los admins puedan autenticarse con sus credenciales reales en lugar del usuario demo.
4. Mover credenciales de Supabase a variables de entorno (`.env`) para no tener contraseñas en el repositorio.
