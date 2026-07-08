# Autenticación y autorización

GameVault incorpora seguridad mediante Spring Security.

La evolución de seguridad se ha realizado en tres pasos:

```text
1. Usuarios en memoria con HTTP Basic.
2. Usuarios y roles persistidos en PostgreSQL.
3. Login con JWT y uso de Authorization: Bearer.
```

## Objetivo

El objetivo de esta parte del proyecto es proteger las operaciones de escritura y administración, manteniendo públicas las consultas principales de catálogo.

## Usuarios y roles

Los usuarios se almacenan en PostgreSQL en la tabla `usuarios`.

Entidad principal:
```text
Usuario
```

Campos relevantes:
* id
* username
* password
* rol
* activo

Roles actuales:

* ADMIN
* USER

En el perfil de desarrollo se crean dos usuarios iniciales:

```text
admin / admin123 → ADMIN
user / user123   → USER
```

Las contraseñas se almacenan cifradas mediante `PasswordEncoder`.

## Autenticación

La autenticación se realiza mediante el endpoint:

```http request
POST /api/v1/auth/login
```

Ejemplo de petición:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Ejemplo de respuesta:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresInSeconds": 3600
}
```
El cliente debe enviar el token en las peticiones protegidas:

```text
Authorization: Bearer <token>
```

## JWT

El token JWT contiene información básica del usuario autenticado:

* emisor;
* usuario;
* fecha de emisión;
* fecha de expiración;
* roles.

Ejemplo conceptual del contenido del token:
```json
{
  "iss": "gamevault",
  "sub": "admin",
  "roles": ["ADMIN"],
  "iat": 1783509185,
  "exp": 1783512785
}
```

La aplicación firma los tokens con una clave simétrica de desarrollo configurada en `application.yaml`.

En un entorno real, esta clave no debería estar escrita directamente en el fichero de configuración, sino gestionarse mediante variables de entorno o un sistema de secretos.

## Autorización por rutas

La autorización se define en `SecurityConfig`.

###Rutas públicas

Las siguientes rutas pueden consultarse sin autenticación:

```http request
GET /api/v1/videojuegos
GET /api/v1/videojuegos/{id}
GET /api/v1/videojuegos/{id}/reviews
GET /api/v1/videojuegos/{id}/reviews/resumen
GET /api/v1/estudios
```

### Rutas para usuarios autenticados

La creación de reviews requiere autenticación con rol `USER` o `ADMIN`:

```http request
POST /api/v1/videojuegos/{id}/reviews
```

### Rutas de administración

Las siguientes rutas requieren rol `ADMIN`:

```http request
POST /api/v1/videojuegos
PUT /api/v1/videojuegos/{id}
DELETE /api/v1/videojuegos/{id}

POST /api/v1/estudios

GET /api/v1/actividad
```

## Códigos de respuesta esperados

La seguridad permite distinguir tres situaciones:

```text
200 / 201
La petición es válida y el usuario tiene permisos.

401 Unauthorized
No se ha enviado token o el token no es válido.

403 Forbidden
El usuario está autenticado, pero no tiene permisos suficientes.
```
Ejemplos:

```text
GET /api/v1/actividad sin token
→ 401

GET /api/v1/actividad con USER
→ 403

GET /api/v1/actividad con ADMIN
→ 200
```

## HTTP Basic

HTTP Basic se utilizó como paso inicial para introducir autenticación y autorización.

En la versión actual, el acceso a rutas protegidas se realiza mediante Bearer Token. HTTP Basic queda desactivado para el acceso a la API.

```
curl -u admin:admin123 /api/v1/actividad
→ 401
```

## Limitaciones actuales

La seguridad actual es suficiente para fines didácticos, pero todavía tiene margen de mejora:

* no hay refresh tokens;
* no hay endpoint de registro;
* no hay gestión de usuarios por API;
* el autor de una review todavía se envía en el cuerpo de la petición;
* la clave JWT es de desarrollo;
* no hay políticas avanzadas de expiración o revocación de tokens;
* los errores de seguridad todavía pueden mejorarse para devolver un JSON más homogéneo.

## Posibles mejoras futuras

Mejoras previstas o posibles:

* utilizar el usuario autenticado como autor real de la review;
* añadir refresh tokens;
* crear endpoints de administración de usuarios;
* mejorar el formato JSON de errores 401 y 403;
* mover la clave JWT a variables de entorno;
* añadir tests de seguridad con `spring-security-test`;
* documentar el flujo de autenticación en OpenAPI.