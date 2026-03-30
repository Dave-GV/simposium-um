# Documentacion tecnica - Modulo Auth/Login

## 1) Objetivo del modulo

Entregar una base estable de autenticacion para CongressManager que permita:

- Autenticar usuarios via API REST.
- Emitir tokens JWT.
- Proteger endpoints privados con Spring Security.
- Servir una pantalla minima para validar el flujo de punta a punta.

Este modulo esta preparado para integracion con los siguientes equipos.

## 2) Alcance implementado

### Endpoints

- `POST /auth/login` (publico)
  - Recibe credenciales.
  - Valida datos con Bean Validation.
  - Devuelve token JWT si login es correcto.

- `GET /api/private/me` (protegido)
  - Requiere header `Authorization: Bearer <token>`.
  - Devuelve identidad y roles del usuario autenticado.

### Frontend tecnico de validacion

- Ruta principal: `/` redirige internamente a `/login-demo/index.html`.
- Ubicacion: `src/main/resources/static/login-demo`.
- Uso: probar login/logout y acceso a endpoint protegido.

## 3) Arquitectura del flujo

1. Cliente envia `email` y `password` a `POST /auth/login`.
2. `AuthController` delega en `AuthService`.
3. `AuthService` valida credenciales de usuario demo configurado por propiedades.
4. `JwtService` genera token firmado.
5. Cliente guarda token y lo envia en llamadas privadas.
6. `JwtAuthenticationFilter` intercepta request, valida JWT y carga el contexto de seguridad.
7. Si token es valido, Spring permite llegar al endpoint privado.

## 4) Clases clave

### Configuracion

- `com.example.simposium.auth.config.SecurityConfig`
  - SecurityFilterChain stateless.
  - Rutas publicas permitidas: `/auth/login`, `/login-demo/**`, `/`, `/error`, `/api/public/**`.
  - Registra `JwtAuthenticationFilter` antes de `UsernamePasswordAuthenticationFilter`.

- `com.example.simposium.auth.config.JwtAuthenticationFilter`
  - Extrae header Bearer.
  - Valida token con `JwtService`.
  - Si es valido, construye autenticacion y la guarda en `SecurityContextHolder`.

- `com.example.simposium.auth.config.JwtProperties`
  - Carga propiedades de JWT (secret y expiracion).

- `com.example.simposium.auth.config.DemoUserProperties`
  - Carga credenciales demo (`app.demo-user.email`, `app.demo-user.password`).

### Aplicacion

- `com.example.simposium.auth.controller.AuthController`
  - Endpoint REST `POST /auth/login`.

- `com.example.simposium.auth.service.AuthService`
  - Orquesta validacion de credenciales y emision de token.

- `com.example.simposium.auth.service.JwtService`
  - Genera, parsea y valida JWT.

- `com.example.simposium.auth.controller.AuthExceptionHandler`
  - Estandariza respuestas de error en JSON.

- `com.example.simposium.auth.controller.PrivateDemoController`
  - Endpoint de prueba para validar acceso autenticado.

## 5) Contratos de datos

### Request

- `LoginRequest`
  - `email`
  - `password`

### Response de exito

- `LoginResponse`
  - `accessToken`
  - `tokenType`
  - `email`

### Response de error

- `ApiErrorResponse`
  - `timestamp`
  - `status`
  - `error`
  - `code`
  - `message`
  - `path`
  - `details` (opcional)

Codigos implementados:

- `AUTH_INVALID_CREDENTIALS` -> 401
- `AUTH_VALIDATION_ERROR` -> 400
- `AUTH_MALFORMED_REQUEST` -> 400

## 6) Configuracion de entorno

Fuente principal: `src/main/resources/application.properties`

Variables esperadas en `.env` (ver `.env.example`):

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JPA_DDL_AUTO`
- `JPA_SHOW_SQL`
- `APP_JWT_SECRET`
- `APP_JWT_EXPIRATION`
- `APP_DEMO_USER_EMAIL`
- `APP_DEMO_USER_PASSWORD`

Notas:

- El modulo login actual funciona con usuario demo configurado por variables.
- Para tests se usa H2 con `src/test/resources/application.properties`.

## 7) Pruebas recomendadas (Postman)

### Login correcto

- `POST /auth/login`
- Body JSON:

```json
{
  "email": "demo@simposium.com",
  "password": "demo123"
}
```

Esperado: `200` con token.

### Login incorrecto

- `POST /auth/login` con password invalida.

Esperado: `401` con `code = AUTH_INVALID_CREDENTIALS`.

### Endpoint privado sin token

- `GET /api/private/me`

Esperado: `401`.

### Endpoint privado con token

- `GET /api/private/me`
- Header `Authorization: Bearer <accessToken>`

Esperado: `200` con email y roles.

## 8) Decisiones de diseno relevantes

- API REST stateless con JWT.
- Manejo de errores consistente para facilitar consumo por frontend y otros equipos.
- Front `login-demo` como herramienta de validacion tecnica, no como UI final.

## 9) Limites actuales (conocidos)

- Usuario de autenticacion en memoria (demo/dev).
- No hay registro/recuperacion de password en este sprint.
- No hay multi-tenant ni roles por evento en este corte.

## 10) Proximos pasos sugeridos para evolucion

1. Migrar autenticacion a repositorio real (`UsuarioRepository`) con password hash en DB.
2. Incluir claims de contexto de evento/tenant en JWT cuando el modelo de datos este listo.
3. Separar totalmente la UI final del `login-demo` tecnico.
4. Agregar refresh token y revocacion segun lineamientos de seguridad del proyecto.

