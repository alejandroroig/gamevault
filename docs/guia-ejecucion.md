# Guía rápida de ejecución

## 1. Abrir el proyecto

El proyecto está preparado para ejecutarse dentro de un Dev Container.

Una vez abierto, el directorio de trabajo es:

```text
/workspace
```

## 2. Comprobar servicios

```bash
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"
```

Los servicios principales definidos en `docker-compose.yaml` son:

- PostgreSQL
- MongoDB
- Redis
- RabbitMQ

## 3. Arrancar la aplicación

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

La API queda disponible en:

```text
http://localhost:8080
```

## 4. Probar login

Usuario de prueba con rol `USER`:

```bash
curl -sS -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}' | jq
```

Usuario de prueba con rol `ADMIN`:

```bash
curl -sS -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq
```

## 5. Probar endpoint protegido

Obtener token de usuario:

```bash
USER_TOKEN=$(curl -sS -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}' | jq -r '.accessToken')
```

Consultar el usuario autenticado:

```bash
curl -sS http://localhost:8080/api/v1/auth/me \
  -H "Authorization: Bearer $USER_TOKEN" | jq
```

Respuesta esperada:

```json
{
  "username": "user",
  "roles": [
    "USER"
  ]
}
```

## 6. Probar creación de review

```bash
curl -i -X POST http://localhost:8080/api/v1/videojuegos/1/reviews \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "puntuacion": 8,
    "comentario": "Review creada desde la guía de ejecución."
  }'
```

La respuesta debe devolver `201 Created` y el campo `autor` debe coincidir con el usuario autenticado.

## 7. Probar endpoint de administración

Obtener token de administrador:

```bash
ADMIN_TOKEN=$(curl -sS -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.accessToken')
```

Consultar actividad:

```bash
curl -sS http://localhost:8080/api/v1/actividad \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq
```

Si se usa el token del usuario `user`, este endpoint debe devolver `403 Forbidden`.

## 8. Swagger UI

La interfaz Swagger UI está disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

La especificación OpenAPI generada está disponible en:

```text
http://localhost:8080/v3/api-docs
```

Para probar endpoints protegidos desde Swagger UI:

1. Hacer login en `POST /api/v1/auth/login`.
2. Copiar el token JWT.
3. Pulsar `Authorize`.
4. Introducir el token en formato Bearer.
5. Ejecutar los endpoints protegidos.

## 9. Ejecutar tests

Ejecutar todos los tests:

```bash
./mvnw test
```

Ejecutar solo el test de integración con Testcontainers:

```bash
./mvnw -Dtest=GamevaultApiTest test
```

## 10. Generar fuentes API-first

El endpoint `GET /api/v1/auth/me` se define primero en el contrato OpenAPI:

```text
src/main/resources/openapi/auth-api.yaml
```

Para regenerar la interfaz y el modelo Java:

```bash
./mvnw generate-sources
```

Los ficheros generados se crean dentro de:

```text
target/generated-sources/openapi
```

Estos ficheros no se deben versionar en Git.

## 11. Consultas útiles a bases de datos

Consultar colecciones MongoDB:

```bash
docker exec gamevault-mongodb-1 mongosh gamevault_db --quiet --eval 'printjson(db.getCollectionNames())'
```

Consultar reviews:

```bash
docker exec gamevault-mongodb-1 mongosh gamevault_db --quiet --eval 'db.review.find().pretty()'
```

Consultar usuarios en PostgreSQL:

```bash
docker exec gamevault-postgres-1 psql -U gamevault_user -d gamevault_db -c "select id, username, rol, activo from usuarios;"
```

Consultar videojuegos en PostgreSQL:

```bash
docker exec gamevault-postgres-1 psql -U gamevault_user -d gamevault_db -c "select id, titulo, precio from videojuegos order by id;"
```

## 12. Comandos de mantenimiento

Compilar:

```bash
./mvnw compile
```

Limpiar y ejecutar tests:

```bash
rm -rf target
./mvnw test
```

Ver estado de Git:

```bash
git status --short
```

Ver etiquetas:

```bash
git tag --sort=v:refname
```
