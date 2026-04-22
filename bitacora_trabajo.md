# Bitácora de Trabajo

## Proyecto

**Nombre:** `simposium-um`  
**Enfoque actual:** construcción del backend Spring Boot con JPA para el módulo base del dashboard.

## Fuentes consideradas

- [`propuesta_unificada.md`](/media/ortiz/KEY/simposium-um/propuesta_unificada.md)
- [`arquitectura_uml.md`](/media/ortiz/KEY/simposium-um/arquitectura_uml.md)
- Estado actual del código en `src/main/java/com/example/simposium`
- Actividades observadas en las capturas: `SCRUM-18`, `SCRUM-22`, `SCRUM-23`, `SCRUM-24`, `SCRUM-25`

## Estado actual del proyecto

Actualmente el proyecto sólo contiene implementación base para:

- Entidad JPA `Ponente`
- Repositorio `PonenteRepository`
- Clase principal `SimposiumApplication`

No existen todavía:

- Modelo `Usuario`
- Modelo `Pago`
- Modelo `Ponencia`
- Repositorios `UserRepository`, `PagoRepository`, `PonenciaRepository`
- DTO `DashboardSummaryDTO`
- Servicio `DashboardService`
- Controlador REST de dashboard

## Observaciones importantes

1. Existe una inconsistencia en la actividad `SCRUM-18`.
   El título indica crear el modelo y repositorio JPA para `Usuario`, pero el contexto de la descripción menciona `Ponente`.

2. El documento UML define para el dashboard el uso de:
   - `UserRepository`
   - `PonenteRepository`
   - `PagoRepository`
   - `PonenciaRepository`

3. El esquema de base de datos en [`propuesta_unificada.md`](/media/ortiz/KEY/simposium-um/propuesta_unificada.md) contiene una tabla `usuarios`, pero no documenta explícitamente tablas `ponentes` ni `ponencias` con el mismo nivel de detalle que el UML.

4. Antes de cerrar la implementación del dashboard conviene validar el lenguaje del dominio:
   - Si el sistema usará `Usuario` o `User`
   - Si `Ponencia` se mapeará a una tabla propia o se derivará de otra estructura
   - Si `Ponente` debe mantenerse como entidad separada del usuario

## Aclaración de dominio confirmada

Queda establecido que:

- `Usuario` es la entidad base del sistema
- Un `Usuario` puede tener diferentes roles
- `PONENTE` es uno de esos roles
- `Usuario` representa la cuenta principal del sistema, incluyendo alumnos, staff, admins y ponentes según el rol asignado

## Decisión de modelado derivada

Para el primer corte del backend se asume lo siguiente:

- La entidad `Usuario` será la raíz del modelo de identidad
- El rol se manejará como atributo o relación, según el alcance que se implemente en esta iteración
- `Ponente` no debe tomarse como una entidad de identidad separada en la definición conceptual del dominio
- Si el código actual conserva una entidad `Ponente`, debe revisarse si representa un perfil de negocio complementario o si después debe consolidarse con `Usuario`
- El nombre `UserRepository` del UML puede mantenerse como nombre técnico del repositorio de `Usuario`, aunque la entidad siga llamándose `Usuario`

## Objetivo de esta bitácora

Registrar el trabajo pendiente para dejar listo un primer backend funcional del dashboard, con entidades mínimas, repositorios, DTOs, servicio y controlador.

## Registro de trabajo pendiente

### 1. Modelado de entidades JPA

#### `SCRUM-18` - Generar modelo de datos y repositorio JPA para `Usuario`

**Descripción operativa**

Crear la entidad `Usuario` como modelo JPA mínimo para que exista persistencia y pueda participar en el resumen del dashboard.

**Alcance mínimo**

- Crear clase `Usuario`
- Anotar con `@Entity`
- Definir tabla asociada
- Definir llave primaria `id`
- Configurar `@GeneratedValue`

**Subtareas**

- [x] Crear `src/main/java/com/example/simposium/model/Usuario.java`
- [x] Agregar `@Entity` y `@Table`
- [x] Declarar atributo `id`
- [x] Configurar `@Id`
- [x] Configurar `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- [x] Definir campos mínimos según decisión de dominio
- [x] Agregar getters y setters

**Nota**

El UML muestra `Usuario { id, nombre, rol }`, mientras que el esquema de base de datos propone más campos para `usuarios`. Dado que `Usuario` es la entidad base con roles, se recomienda implementar al menos:

- `id`
- `nombre` o `firstname` / `lastname`
- `rol`

#### Entidades adicionales requeridas por el dashboard

Para soportar `DashboardService.getDashboardSummary()`, también hacen falta estas entidades:

- [ ] `Pago`
- [ ] `Ponencia`

**Criterio mínimo**

Cada entidad debe existir con `id` y configuración JPA suficiente para que `JpaRepository.count()` funcione.

**Situación actual**

- [x] `Usuario` ya existe en el proyecto
- [x] `Ponente` ya existe en el proyecto
- [x] `Pago` ya existe en el proyecto
- [x] `Ponencia` ya existe en el proyecto

**Observación de diseño**

Aunque el proyecto hoy tiene una entidad `Ponente`, la definición de dominio confirmada indica que `Usuario` es la entidad base y `PONENTE` es un rol. Antes de extender demasiado `Ponente`, conviene decidir si:

- se conservará `Ponente` como entidad complementaria con información específica del expositor, vinculada a `Usuario`, o
- se migrará más adelante para que toda la identidad viva en `Usuario`

### 2. Repositorios Spring Data JPA

#### `SCRUM-18` - Repositorio de `Usuario`

- [x] Crear `src/main/java/com/example/simposium/repository/UserRepository.java`
- [x] Extender `JpaRepository<Usuario, Long>`

**Observación**

Hay que decidir si el nombre final será `UserRepository` o `UsuarioRepository`. El UML usa `UserRepository`, por lo que conviene respetarlo para no romper consistencia con el diseño, aun cuando la entidad de dominio sea `Usuario`.

#### `SCRUM-24` - Generar métodos `count()` en repositorios

**Descripción operativa**

Los repositorios que extienden `JpaRepository` ya heredan `count()`, por lo que esta actividad probablemente se resuelve creando correctamente los repositorios faltantes.

**Repositorios requeridos**

- [x] `UserRepository`
- [x] `PonenteRepository`
- [x] `PagoRepository`
- [x] `PonenciaRepository`

**Validación**

- [x] Confirmar que todos extienden `JpaRepository<Entidad, Long>`
- [x] Confirmar que `count()` está disponible sin implementación manual

### 3. DTOs

#### `SCRUM-25` - Generar DTOs

**DTO prioritario**

`DashboardSummaryDTO`

**Campos esperados según UML**

- `totalUsers`
- `totalPonentes`
- `totalPagos`
- `totalPonencias`

**Pendiente**

- [x] Crear `src/main/java/com/example/simposium/dto/DashboardSummaryDTO.java`
- [x] Agregar atributos
- [x] Agregar constructor vacío
- [x] Agregar constructor completo o builder simple
- [x] Agregar getters y setters

### 4. Lógica de negocio

#### `SCRUM-23` - Generar `DashboardService`

**Descripción operativa**

Implementar un servicio que consulte los `count()` de los cuatro repositorios y regrese un `DashboardSummaryDTO`.

**Pendiente**

- [x] Crear `src/main/java/com/example/simposium/service/DashboardService.java`
- [x] Anotar con `@Service`
- [x] Inyectar `UserRepository`
- [x] Inyectar `PonenteRepository`
- [x] Inyectar `PagoRepository`
- [x] Inyectar `PonenciaRepository`
- [x] Implementar método `getDashboardSummary()`
- [x] Mapear conteos hacia `DashboardSummaryDTO`

**Resultado esperado**

El método debe devolver:

- total de usuarios
- total de ponentes
- total de pagos
- total de ponencias

### 5. Exposición REST

#### `SCRUM-22` - Generar controlador `DashboardController`

**Descripción operativa**

Crear un controlador REST que llame a `getDashboardSummary()` y exponga el endpoint del dashboard.

**Pendiente**

- [x] Crear `src/main/java/com/example/simposium/controller/DashboardController.java`
- [x] Anotar con `@RestController`
- [x] Definir ruta base del endpoint
- [x] Inyectar `DashboardService`
- [x] Implementar método `getDashboardSummary()`
- [x] Retornar `ResponseEntity<DashboardSummaryDTO>`

**Ruta esperada según UML**

- [x] Exponer `GET /api/dashboard`

**Observación**

El UML nombra el controlador como `DashboardRestController`, mientras que la actividad lo llama `DashboardController`. Conviene elegir un solo nombre y mantenerlo en todo el código.

### 6. Verificación técnica

#### Checklist de validación

- [x] El proyecto compila con Maven
- [x] Las entidades JPA son detectadas por Spring Boot
- [x] Los repositorios se registran sin errores
- [x] El endpoint `GET /api/dashboard` responde correctamente
- [x] El DTO se serializa a JSON
- [x] Los `count()` se ejecutan sin implementación manual

**Validación ejecutada**

- [x] `./mvnw clean test` exitoso usando JDK 21 local
- [x] Prueba de contexto con base H2 en perfil `test`
- [x] Prueba del servicio `DashboardService`
- [x] Prueba del controlador `DashboardController`

## Orden sugerido de implementación

1. Resolver nombres de dominio y convenciones: `Usuario/User`, `DashboardController/DashboardRestController`
2. Crear entidades faltantes mínimas: `Usuario`, `Pago`, `Ponencia`
3. Crear repositorios faltantes
4. Crear `DashboardSummaryDTO`
5. Implementar `DashboardService`
6. Implementar controlador REST
7. Ejecutar validación de compilación y endpoint

## Riesgos y decisiones abiertas

- [ ] Confirmar si la tabla de `Usuario` debe llamarse `usuarios` o `user`
- [ ] Confirmar si `Usuario` tendrá campos mínimos o completos según el esquema
- [ ] Confirmar si `Ponencia` tiene tabla propia y cuál será su nombre real
- [ ] Confirmar si `Pago` y `Ponencia` deben quedar como entidades mínimas temporales o completas
- [ ] Confirmar si el dashboard requiere autenticación desde esta primera iteración
- [ ] Definir si `Ponente` seguirá existiendo como entidad adicional vinculada a `Usuario` o si sólo será un rol

## Decisiones ya resueltas

- [x] `Usuario` es la entidad base del sistema
- [x] `Usuario` puede tener distintos roles
- [x] `PONENTE` es uno de los roles posibles de `Usuario`

## Próximo corte de trabajo recomendado

Primer entregable técnico funcional:

- Entidades mínimas: `Usuario`, `Pago`, `Ponencia`
- Repositorios JPA correspondientes
- `DashboardSummaryDTO`
- `DashboardService`
- `DashboardController`
- Endpoint `GET /api/dashboard` respondiendo con conteos

## Bitácora de avance

### 2026-04-22

- [x] Se integró la documentación base al proyecto
- [x] Se revisó el estado actual del código
- [x] Se identificó que sólo existe `Ponente` y su repositorio
- [x] Se consolidaron las tareas visibles en una bitácora ejecutable
- [x] Se creó la entidad `Usuario`
- [x] Se crearon las entidades mínimas `Pago` y `Ponencia`
- [x] Se crearon los repositorios `UserRepository`, `PagoRepository` y `PonenciaRepository`
- [x] Se creó `DashboardSummaryDTO`
- [x] Se implementó `DashboardService`
- [x] Se implementó `DashboardController`
- [x] Se agregó configuración mínima de seguridad para habilitar `GET /api/dashboard`
- [x] Se dejó configuración de pruebas aislada con H2
- [x] Se validó `mvn clean test` con JDK 21 local
