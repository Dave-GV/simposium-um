# CongressManager - Modulo de Login (Auth)

Este repositorio contiene la base del modulo de autenticacion para el proyecto CongressManager.
El objetivo es exponer un login REST con JWT que el resto de equipos pueda consumir.

## Configuracion de credenciales con .env

Para evitar subir secretos al repositorio, la app lee variables locales desde un archivo `.env`.

1. Crea un archivo `.env` en la raiz del proyecto.
2. Copia la base desde `.env.example`.
3. Ajusta valores de acuerdo con tu entorno local.

Spring Boot ya esta configurado para cargar este archivo con:

`spring.config.import=optional:file:.env[.properties]`

## Alcance actual del modulo

- Login REST en `POST /auth/login`.
- Generacion de JWT y validacion para rutas privadas.
- Endpoint protegido de verificacion en `GET /api/private/me`.
- Frontend estatico minimo en `src/main/resources/static/login-demo` para pruebas de integracion.

Nota para el equipo: la vista `login-demo` es una pantalla de validacion tecnica del flujo auth.
No representa la UX final del producto.

## Flujo rapido

1. Usuario envia email/password a `POST /auth/login`.
2. Si credenciales son validas, backend responde `200` con `accessToken`.
3. Cliente envia `Authorization: Bearer <token>` a endpoints privados.
4. `JwtAuthenticationFilter` valida token y permite acceso.

## Contrato API del login

- Exito: `200 OK` con `accessToken`, `tokenType`, `email`.
- Error credenciales: `401 Unauthorized` con body estandar (`AUTH_INVALID_CREDENTIALS`).
- Error de validacion: `400 Bad Request` con body estandar (`AUTH_VALIDATION_ERROR`).

## Documentacion del modulo para el equipo

Para detalle tecnico completo (arquitectura, clases, endpoints, pruebas y siguientes pasos), revisa:

`docs/auth-login-module.md`
