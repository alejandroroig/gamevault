# GameVault

GameVault es una API didáctica desarrollada con Spring Boot para trabajar contenidos de desarrollo backend, persistencia, seguridad, mensajería, documentación de APIs y pruebas de integración en ciclos formativos de Informática.

El proyecto está planteado como un monolito modular pragmático, orientado a docencia en DAM, DAW y cursos de especialización relacionados con cloud, despliegue y desarrollo backend.

## Objetivos didácticos

El proyecto permite trabajar:

- creación de APIs REST con Spring Boot;
- persistencia relacional con PostgreSQL y Spring Data JPA;
- persistencia documental con MongoDB;
- caché con Redis;
- mensajería asíncrona con RabbitMQ;
- autenticación y autorización con Spring Security y JWT;
- documentación de APIs con OpenAPI y Swagger UI;
- ejemplo mínimo de enfoque API-first;
- pruebas unitarias, de controlador e integración con Testcontainers;
- uso de Docker Compose y Dev Containers como entorno de desarrollo reproducible.

## Tecnologías principales

- Java 25
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- MongoDB
- Redis
- RabbitMQ
- Spring Security
- JWT
- OpenAPI / Swagger UI
- OpenAPI Generator
- Testcontainers
- Docker Compose
- Dev Containers
- Maven

## Arquitectura general

GameVault está organizado como un monolito modular. La aplicación se divide en módulos funcionales:

```text
com.aleroig.gamevault
├── actividad
├── catalogo
├── config
├── exception
├── reviews
└── seguridad
```

El módulo `catalogo` gestiona estudios y videojuegos mediante PostgreSQL y JPA.

El módulo `reviews` gestiona las reseñas mediante MongoDB.

El módulo `actividad` registra acciones relevantes de la aplicación.

El módulo `seguridad` gestiona usuarios, autenticación JWT y autorización por roles.

La comunicación entre módulos se ha diseñado evitando acoplamientos innecesarios. Para consultas síncronas entre módulos se usan servicios públicos internos. Para reacciones asíncronas ante eventos de dominio se usa RabbitMQ.

Más información:

- [`docs/arquitectura/modulos-y-desacoplamiento.md`](docs/arquitectura/modulos-y-desacoplamiento.md)
- [`docs/arquitectura/decisiones.md`](docs/arquitectura/decisiones.md)

## Arranque del entorno

El proyecto está preparado para ejecutarse dentro de un Dev Container.

Desde el IDE compatible con Dev Containers, abrir el proyecto dentro del contenedor de desarrollo.

El servicio principal del contenedor usa el directorio:

```text
/workspace
```

Los servicios auxiliares definidos en `docker-compose.yaml` son:

- PostgreSQL
- MongoDB
- Redis
- RabbitMQ

Para comprobar los contenedores:

```bash
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"
```

## Ejecutar la aplicación

Dentro del Dev Container:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

La API queda disponible en:

```text
http://localhost:8080
```

## Usuarios de desarrollo

El perfil `dev` crea datos iniciales y usuarios de prueba.

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin123` | `ADMIN` |
| `user` | `user123` | `USER` |

## Autenticación JWT

Para obtener un token:

```bash
curl -sS -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}' | jq
```

Ejemplo de uso del token:

```bash
USER_TOKEN=$(curl -sS -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}' | jq -r '.accessToken')

curl -sS http://localhost:8080/api/v1/auth/me \
  -H "Authorization: Bearer $USER_TOKEN" | jq
```

Más información:

- [`docs/seguridad/autenticacion-y-autorizacion.md`](docs/seguridad/autenticacion-y-autorizacion.md)

## Endpoints principales

| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| `GET` | `/api/v1/videojuegos` | Lista videojuegos | Público |
| `GET` | `/api/v1/videojuegos/{id}` | Consulta un videojuego | Público |
| `POST` | `/api/v1/videojuegos` | Crea videojuego | `ADMIN` |
| `PUT` | `/api/v1/videojuegos/{id}` | Actualiza videojuego | `ADMIN` |
| `DELETE` | `/api/v1/videojuegos/{id}` | Elimina videojuego | `ADMIN` |
| `GET` | `/api/v1/estudios` | Lista estudios | Público |
| `POST` | `/api/v1/estudios` | Crea estudio | `ADMIN` |
| `GET` | `/api/v1/videojuegos/{id}/reviews` | Lista reviews de un videojuego | Público |
| `POST` | `/api/v1/videojuegos/{id}/reviews` | Crea review | `USER` o `ADMIN` |
| `GET` | `/api/v1/actividad` | Consulta actividad | `ADMIN` |
| `POST` | `/api/v1/auth/login` | Login JWT | Público |
| `GET` | `/api/v1/auth/me` | Usuario autenticado | Autenticado |

## Swagger UI y OpenAPI

La documentación automática de la API está disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

La especificación OpenAPI generada está disponible en:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI permite probar endpoints protegidos usando el botón `Authorize` e introduciendo un token JWT.

## Ejemplo API-first

Además de generar documentación OpenAPI desde los controladores existentes, el proyecto incluye un ejemplo mínimo de enfoque API-first.

El endpoint:

```text
GET /api/v1/auth/me
```

se define primero en el contrato:

```text
src/main/resources/openapi/auth-api.yaml
```

A partir de ese contrato, Maven genera una interfaz Java y un modelo de respuesta mediante OpenAPI Generator. La aplicación implementa después esa interfaz en un controlador propio.

Esto permite comparar:

```text
Código primero:
controladores Java → documentación OpenAPI generada

API-first:
contrato OpenAPI → código Java generado → implementación
```

## Tests

Ejecutar todos los tests:

```bash
./mvnw test
```

Ejecutar solo el test de integración con Testcontainers:

```bash
./mvnw -Dtest=GamevaultApiTest test
```

El proyecto incluye pruebas de integración con Testcontainers. Estas pruebas levantan contenedores efímeros de:

- PostgreSQL
- MongoDB
- Redis
- RabbitMQ

Esto permite validar la aplicación contra infraestructura real sin depender del `docker-compose` de desarrollo.

## Comandos útiles

Arrancar aplicación:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Ejecutar tests:

```bash
./mvnw test
```

Generar fuentes API-first:

```bash
./mvnw generate-sources
```

Compilar:

```bash
./mvnw compile
```

Ver contenedores:

```bash
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"
```

Consultar colecciones MongoDB:

```bash
docker exec gamevault-mongodb-1 mongosh gamevault_db --quiet --eval 'printjson(db.getCollectionNames())'
```

Consultar usuarios en PostgreSQL:

```bash
docker exec gamevault-postgres-1 psql -U gamevault_user -d gamevault_db -c "select id, username, rol, activo from usuarios;"
```

## Hitos del proyecto

El proyecto se ha desarrollado de forma incremental mediante issues, ramas, pull requests y etiquetas.

Algunos hitos relevantes:

- `v0.13-repositorios-spring-data`
- `v0.15-reviews-anidadas`
- `v0.20-filtro-plataforma-jsonb`
- `v0.23-actividad-asincrona`
- `v0.24-actividad-rabbitmq`
- `v0.26-eventos-dominio-videojuegos`
- `v0.27-api-v1`
- `v0.28-seguridad-basica`
- `v0.29-usuarios-persistidos`
- `v0.30-jwt-auth`
- `v0.33-testcontainers-integracion`
- `v0.34-swagger-openapi`
- `v0.35-api-first-auth-me`

## Uso docente

GameVault puede utilizarse como proyecto base para trabajar de forma progresiva:

1. API REST básica.
2. Persistencia con JPA.
3. Persistencia documental con MongoDB.
4. Validaciones y manejo de errores.
5. DTOs y separación entre entidades y API.
6. Caché.
7. Eventos y mensajería.
8. Seguridad JWT.
9. Documentación OpenAPI.
10. Testcontainers.
11. Contratos API-first.

La evolución por etiquetas permite volver a estados concretos del proyecto para explicar cada decisión técnica de forma aislada.
