# GameVault

GameVault es un proyecto docente basado en una API backend para gestionar videojuegos, estudios y reseñas de usuarios.

El proyecto se utilizará como laboratorio integrado de los módulos de Acceso a Datos y Programación de Servicios y Procesos.

## Objetivos técnicos

- Construir una API REST con Spring Boot.
- Trabajar persistencia relacional con PostgreSQL y Spring Data JPA.
- Incorporar datos semiestructurados mediante JSONB.
- Usar MongoDB para reseñas.
- Aplicar Redis como caché.
- Practicar seguridad de APIs con Spring Security y JWT.
- Introducir mensajería asíncrona con RabbitMQ.
- Documentar la API con OpenAPI/Swagger.
- Utilizar GitHub de forma profesional mediante ramas, issues, commits, pull requests y tags.

## Rama principal

La rama estable del proyecto es master.

## Estado base

El punto de partida del laboratorio está marcado con el tag `v0.0-base`.

## Arquitectura y seguridad

GameVault es un monolito modular construido con Spring Boot.

Actualmente incluye:

- catálogo de videojuegos y estudios con PostgreSQL;
- reviews almacenadas en MongoDB;
- caché con Redis;
- eventos de dominio con RabbitMQ;
- registro de actividad;
- seguridad con usuarios persistidos y JWT.

La comunicación entre módulos se resuelve de dos formas:

- consultas síncronas mediante APIs públicas internas;
- reacciones asíncronas mediante eventos de dominio publicados en RabbitMQ.

La API está versionada bajo `/api/v1`.

La autenticación se realiza mediante:

```http
POST /api/v1/auth/login
```

Las rutas protegidas usan:
```http
Authorization: Bearer <token>
```

## Documentación

- [Flujo de trabajo con Git y GitHub](docs/00-flujo-git-github.md)
- [Estado inicial de GameVault](docs/01-estado-inicial-gamevault.md)
- [Dev Container de GameVault](docs/02-devcontainer.md)