# Arquitectura modular y desacoplamiento

GameVault se plantea como un monolito modular. La aplicación se despliega como un único servicio Spring Boot, pero internamente se organiza en módulos funcionales con responsabilidades diferenciadas.

## Módulos actuales

La estructura principal del proyecto se organiza alrededor de los siguientes módulos:

- `catalogo`: gestiona estudios y videojuegos.
- `reviews`: gestiona reseñas asociadas a videojuegos.
- `actividad`: registra actividad relevante producida en la aplicación.
- `seguridad`: gestiona autenticación, usuarios, roles y autorización.
- `config`: contiene configuración transversal de persistencia, mensajería y arranque.

El objetivo no es crear microservicios, sino evitar que todos los paquetes accedan libremente a los detalles internos de los demás.

## Problema inicial de acoplamiento

Inicialmente existían dependencias directas entre módulos. Por ejemplo:

```text
reviews → VideojuegoRepository
```

El módulo `reviews` consultaba directamente el repositorio de catalogo para comprobar si un videojuego existía.

También existía una dependencia en sentido contrario:

```text
catalogo → ReviewService
```

El módulo `catalogo` llamaba directamente al servicio de `reviews` para borrar las reseñas cuando se eliminaba un videojuego.

Aunque estas soluciones funcionaban, hacían que los módulos conocieran demasiados detalles internos entre sí.

## Comunicación síncrona mediante API pública interna

Cuando un módulo necesita una respuesta inmediata de otro módulo, se utiliza una API pública interna.

El caso principal es la creación o consulta de reseñas:

```text
reviews → catalogo.api.CatalogoConsultaService
```

El módulo `reviews` necesita saber si el videojuego existe antes de crear una reseña o devolver sus reseñas. Como necesita una respuesta inmediata, se mantiene una comunicación síncrona.

La diferencia importante es que `reviews` ya no accede directamente a `VideojuegoRepository`, sino a una interfaz pública del módulo `catalogo`.

```java
public interface CatalogoConsultaService {
    boolean existeVideojuego(Long videojuegoId);
}
```

De este modo, `catalogo` mantiene ocultos sus detalles internos de persistencia.

## Comunicación asíncrona mediante eventos de dominio

Cuando un módulo solo necesita informar de que algo ha ocurrido, se utilizan eventos de dominio.

El caso principal es la eliminación de videojuegos:

```text
catalogo → VideojuegoEvent → RabbitMQ → reviews
catalogo → VideojuegoEvent → RabbitMQ → actividad
```

Cuando se elimina un videojuego, el módulo `catalogo` publica un evento de dominio:

```text
VIDEOJUEGO_ELIMINADO
```

El módulo `reviews` consume ese evento y borra las reseñas asociadas.

El módulo `actividad` consume el mismo evento y registra la actividad.

De esta forma, `catalogo` ya no necesita llamar directamente a `ReviewService`.

## Eventos de dominio de videojuegos

Los eventos de dominio representan hechos relevantes ocurridos en el módulo `catalogo`.

Eventos actuales:

* `VIDEOJUEGO_CREADO`
* `VIDEOJUEGO_ACTUALIZADO`
* `VIDEOJUEGO_ELIMINADO`

Estructura simplificada del evento:

```java
public record VideojuegoEvent(
    String tipo,
    Long videojuegoId,
    String titulo
) {
}
```

## RabbitMQ

RabbitMQ se utiliza como mecanismo de mensajería para distribuir eventos de dominio.

La configuración actual utiliza un exchange de videojuegos y varias colas consumidoras:

```text
Exchange:
gamevault.videojuego.exchange

Colas:
gamevault.actividad.videojuego.queue
gamevault.reviews.videojuego.queue
```

La cola de actividad escucha todos los eventos de videojuegos.

```text
videojuego.*
```

La cola de reviews escucha únicamente la eliminación de videojuegos.

```text
videojuego.eliminado
```

## Decisión arquitectónica

La arquitectura actual diferencia dos tipos de comunicación:

```text
Consulta inmediata:
reviews → catalogo.api.CatalogoConsultaService

Reacción ante eventos:
catalogo → RabbitMQ → actividad / reviews
```

Esta distinción permite explicar dos formas habituales de desacoplamiento:

* API pública interna para consultas síncronas.
* Eventos de dominio para reacciones asíncronas.

## Ventajas didácticas

Esta evolución permite trabajar varios conceptos importantes:

* separación de responsabilidades;
* monolito modular;
* acoplamiento entre módulos;
* API pública interna;
* mensajería asíncrona;
* eventos de dominio;
* consistencia eventual;
* diferencia entre llamada directa y publicación de eventos.

## Estado actual

El proyecto no pretende ser una arquitectura de microservicios. Todos los módulos siguen dentro de la misma aplicación Spring Boot.

Sin embargo, la comunicación entre módulos se ha hecho más explícita y controlada.
