# Arquitectura de la Aplicación — Diagrama de Clases UML

## Descripción General

Este documento describe la arquitectura de una aplicación con frontend (Cliente) y backend (Spring Boot REST API), organizada en capas: Modelos, Controladores, Servicios, Repositorios y DTOs.

---

## 1. Models (Entidades JPA)

Entidades del dominio persistidas en base de datos mediante JPA.

### `Usuario`
| Campo | Tipo |
|---|---|
| id | Long |
| nombre | String |
| rol | String |

### `Ponente`
> Sin campos detallados en el diagrama.

### `Pago`
> Sin campos detallados en el diagrama.

### `Ponencia`
> Sin campos detallados en el diagrama.

---

## 2. Cliente (Frontend App)

Capa de presentación de la aplicación. Se comunica con el backend mediante HTTP.

### `DashboardView` *(«Page»)*
Vista principal del dashboard. Orquesta los componentes y servicios del frontend.

**Relaciones:**
- Usa (x4): `AppbarComponent`, `InfoCardComponent`, `ApiService`, `SidebarComponent`
- `AppbarComponent` obtiene info de usuario desde `AuthService`
- `ApiService` solicita datos al backend vía **HTTP GET /api/dashboard** (Bearer Token)
- `AuthService` se autentica vía **HTTP POST /api/auth/login**

### `AppbarComponent` *(«Component»)*
Barra de navegación superior. Obtiene información del usuario desde `AuthService`.

### `InfoCardComponent` *(«Component»)*
Componente de tarjetas informativas del dashboard.

### `ApiService` *(«Service»)*
Servicio encargado de las peticiones HTTP al backend.

**Endpoints utilizados:**
- `GET /api/dashboard` — con Bearer Token
- Recibe JSON con el resumen del dashboard (`DashboardSummaryDTO`)

### `SidebarComponent` *(«Component»)*
Barra lateral de navegación.

### `AuthService` *(«Service»)*
Servicio de autenticación del frontend.

**Endpoints utilizados:**
- `POST /api/auth/login` — retorna JSON (`AuthResponseDTO`)

---

## 3. Controllers (REST API — Spring Boot)

Capa de controladores REST. Reciben peticiones HTTP y delegan en los servicios.

### `DashboardRestController` *(«@RestController»)*
| Atributo / Método | Detalle |
|---|---|
| -dashboardService | DashboardService |
| +getDashboardSummary(authentication: Authentication) | ResponseEntity\<DashboardSummaryDTO\> |

**Relaciones:**
- Inyecta `DashboardService`
- Retorna JSON empaquetado como `DashboardSummaryDTO`

### `AuthController` *(«@RestController»)*
| Método | Detalle |
|---|---|
| +login(credentials: LoginRequestDTO) | ResponseEntity\<AuthResponseDTO\> |

**Relaciones:**
- Retorna JSON como `AuthResponseDTO`

---

## 4. Services (Lógica de Negocio)

Capa de servicios. Contiene la lógica de negocio y coordina los repositorios.

### `DashboardService` *(«@Service»)*
| Atributo / Método | Detalle |
|---|---|
| -userRepository | UserRepository |
| -ponenteRepository | PonenteRepository |
| -pagoRepository | PagoRepository |
| -ponenciaRepository | PonenciaRepository |
| +getDashboardSummary() | DashboardSummaryDTO |

**Relaciones:**
- Usa: `UserRepository`, `PonenteRepository`, `PagoRepository`, `PonenciaRepository`
- Empaqueta datos en `DashboardSummaryDTO`

---

## 5. Repositories (Acceso a Datos)

Capa de repositorios. Interfaces Spring Data JPA para acceso a la base de datos.

### `PonenteRepository` *(«Spring Data»)*
| Método | Retorno |
|---|---|
| +count() | long |

### `PagoRepository` *(«Spring Data»)*
| Método | Retorno |
|---|---|
| +count() | long |

### `PonenciaRepository` *(«Spring Data»)*
| Método | Retorno |
|---|---|
| +count() | long |

### `UserRepository` *(«Spring Data»)*
| Método | Retorno |
|---|---|
| +count() | long |

---

## 6. DTOs (Transferencia de Datos JSON)

Objetos de transferencia de datos entre backend y frontend.

### `DashboardSummaryDTO`
| Campo | Tipo |
|---|---|
| totalUsers | Integer |
| totalPonentes | Integer |
| totalPagos | Integer |
| totalPonencias | Integer |

**Métodos:** `+get() / +set()` (getters y setters estándar)

### `AuthResponseDTO`
| Campo | Tipo |
|---|---|
| token | String |
| username | String |
| role | String |
| avatarUrl | String |

---

## Flujo de Comunicación

```
[Frontend]
  DashboardView
    ├── AppbarComponent → AuthService → POST /api/auth/login → AuthController → AuthResponseDTO
    └── ApiService      → GET /api/dashboard (Bearer Token)
                            → DashboardRestController
                                → DashboardService
                                    ├── UserRepository.count()
                                    ├── PonenteRepository.count()
                                    ├── PagoRepository.count()
                                    └── PonenciaRepository.count()
                                → DashboardSummaryDTO (JSON)
```
