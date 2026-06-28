# Flujo de trabajo con Git y GitHub

Este proyecto se trabajará mediante un flujo sencillo basado en ramas cortas, issues, commits semánticos, pull requests y tags.

## Flujo general

1. Crear o seleccionar un issue.
2. Crear una rama desde `master`.
3. Realizar cambios pequeños y comprobables.
4. Hacer commits semánticos.
5. Subir la rama a GitHub.
6. Abrir una Pull Request.
7. Revisar los cambios.
8. Fusionar a `master`.
9. Crear un tag cuando se cierre una fase relevante.

## Tipos de commits

Ejemplos:

- `docs: añade documentación inicial del proyecto`
- `feat: añade configuración de Dev Container`
- `fix: corrige nombre de VideojuegoController`
- `refactor: reorganiza paquetes del catálogo`
- `test: añade pruebas de ReviewService`
- `chore: actualiza configuración del proyecto`

## Ramas

La rama principal del proyecto es `master`.

Las ramas de trabajo seguirán este formato:

- `feature/docs-iniciales`
- `feature/devcontainer`
- `feature/security-jwt`
- `feature/openapi`
- `feature/rabbitmq-review-event`

## Tags

Los tags se utilizarán para marcar puntos estables del proyecto.

Ejemplos:

- `v0.0-base`
- `v0.1-docs-iniciales`
- `v0.2-devcontainer`
- `v1.0-security-jwt`