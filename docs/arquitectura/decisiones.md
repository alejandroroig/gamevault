# Decisiones arquitectónicas

Este documento resume algunas decisiones relevantes tomadas durante la evolución de GameVault.

## 1. Monolito modular frente a microservicios

GameVault se mantiene como monolito modular.

La aplicación se despliega como una única aplicación Spring Boot, pero internamente se organiza en módulos funcionales.

Motivo:

- menor complejidad operativa;
- más adecuado para un contexto docente;
- permite explicar buenas prácticas de modularidad sin introducir la complejidad de microservicios;
- facilita el uso conjunto de PostgreSQL, MongoDB, Redis y RabbitMQ en un único proyecto.

## 2. API pública interna entre módulos

Cuando un módulo necesita consultar información de otro módulo de forma inmediata, se utiliza una interfaz pública interna.

Ejemplo:

```text
reviews → catalogo.api.CatalogoConsultaService
```

Esta decisión evita que reviews acceda directamente a repositorios internos de `catalogo`.

## 3. Eventos de dominio para reacciones asíncronas

Cuando un módulo necesita informar de que algo ha ocurrido, publica un evento de dominio.

Ejemplo:
```text
catalogo publica VIDEOJUEGO_ELIMINADO
reviews borra las reviews asociadas
actividad registra la actividad
```

Esta decisión reduce el acoplamiento directo entre módulos.

## 4. RabbitMQ para mensajería

RabbitMQ se utiliza para distribuir eventos de dominio entre módulos.

No se utiliza para consultas que requieren respuesta inmediata.

## 5. API versionada

La API pública se versiona bajo `/api/v1`.

Motivo:

* estabilizar las rutas antes de introducir seguridad;
* facilitar futuras evoluciones;
* preparar la documentación OpenAPI.

## 6. Seguridad con JWT

La seguridad evolucionó en varios pasos:

```text
HTTP Basic con usuarios en memoria
→ usuarios persistidos en PostgreSQL
→ JWT con Authorization: Bearer
```

Esta evolución permite explicar progresivamente autenticación, autorización, roles, persistencia de usuarios y uso de tokens.