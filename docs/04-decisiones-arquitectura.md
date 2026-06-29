# Decisiones arquitectónicas iniciales de GameVault

Este documento recoge las principales decisiones arquitectónicas del proyecto GameVault.

El objetivo no es definir una arquitectura empresarial compleja, sino establecer una base técnica clara, progresiva y defendible para un proyecto docente de DAM/DAW.

## 1. Enfoque general

GameVault se plantea como una API backend desarrollada con Spring Boot.

La arquitectura elegida es un monolito modular pragmático. Esto significa que la aplicación se despliega como una única aplicación, pero el código se organiza por áreas funcionales para facilitar su mantenimiento y evolución.

No se plantea inicialmente una arquitectura de microservicios, ya que añadiría una complejidad innecesaria para los objetivos didácticos del proyecto.

Tampoco se plantea una arquitectura hexagonal estricta como punto de partida. Aunque algunos de sus principios pueden ser útiles más adelante, el objetivo inicial es mantener una estructura sencilla, comprensible y profesional.

## 2. Monolito modular pragmático

GameVault se organiza como un monolito modular porque permite trabajar conceptos reales de desarrollo backend sin introducir una complejidad excesiva.

Este enfoque permite:

* mantener una única aplicación Spring Boot;
* evitar problemas prematuros de comunicación entre servicios;
* organizar el código por funcionalidades;
* facilitar la evolución progresiva del proyecto;
* preparar futuras ampliaciones sin rediseñar todo desde cero.

La aplicación podrá crecer incorporando nuevas áreas, como seguridad, estadísticas, mensajería o documentación OpenAPI, sin abandonar este enfoque.

## 3. Organización por funcionalidad

Se prioriza una estructura por funcionalidad frente a una estructura técnica global.

En lugar de organizar todo el proyecto en carpetas generales como `controller`, `service`, `repository` y `model`, se prefiere agrupar el código según el área funcional a la que pertenece.

La estructura objetivo de GameVault será un monolito modular pragmático:

```text
com.aleroig.gamevault
├── catalogo
│   ├── api
│   │   └── dto
│   ├── dominio
│   ├── infraestructura
│   ├── VideojuegoController
│   ├── VideojuegoService
│   └── VideojuegoRepository
├── reviews
│   ├── api
│   │   └── dto
│   ├── dominio
│   ├── infraestructura
│   ├── ReviewController
│   ├── ReviewService
│   └── ReviewRepository
├── estadisticas
├── seguridad
├── config
└── exception
```

Esta estructura no implica aplicar arquitectura hexagonal estricta. La idea es mantener una organización sencilla, pero preparada para crecer.

Los paquetes principales representan áreas funcionales del proyecto:

* `catalogo`: videojuegos, estudios y consultas relacionadas con el catálogo.
* `reviews`: reseñas de usuarios sobre videojuegos.
* `estadisticas`: futuro módulo para métricas, puntuaciones medias o resúmenes.
* `seguridad`: futuro módulo para usuarios, autenticación, roles y JWT.
* `config`: configuración general de la aplicación.
* `exception`: gestión común de errores.

Dentro de cada módulo funcional podrán usarse subpaquetes cuando el tamaño del módulo lo justifique:

* `api`: controladores REST y elementos relacionados con la entrada/salida HTTP.
* `api.dto`: objetos de transferencia de datos usados por la API.
* `dominio`: entidades, objetos de dominio o reglas propias del módulo.
* `infraestructura`: detalles técnicos, adaptadores, repositorios concretos o integraciones externas si fueran necesarios.

No será obligatorio crear todos estos subpaquetes desde el principio. Si un módulo es pequeño, podrá mantenerse más plano. La estructura crecerá de forma progresiva, evitando crear carpetas vacías o capas innecesarias.

El criterio general será que el proyecto resulte fácil de entender para el alumnado, pero que también muestre una forma razonablemente profesional de organizar una aplicación backend.

## 4. Evolución futura de módulos

A medida que GameVault crezca, podrán añadirse nuevos paquetes funcionales:

```text
estadisticas
seguridad
notificaciones
```

Estos paquetes se incorporarán solo cuando exista una necesidad real dentro del proyecto.

Por ejemplo:

* `seguridad` se añadirá al introducir autenticación, usuarios, roles y JWT.
* `estadisticas` se añadirá al calcular métricas de videojuegos y reseñas.
* `notificaciones` o eventos se podrán añadir si se trabaja mensajería asíncrona con RabbitMQ.

## 5. Criterio sobre microservicios

No se utilizarán microservicios como arquitectura inicial.

Aunque GameVault trabaja conceptos que podrían aparecer en sistemas distribuidos, como APIs, bases de datos, caché o mensajería, el proyecto no necesita dividirse en múltiples servicios independientes.

La prioridad es que el alumnado comprenda bien:

* el diseño de una API REST;
* la persistencia relacional y documental;
* la validación de datos;
* la gestión de errores;
* la seguridad básica;
* el uso de caché;
* la mensajería asíncrona;
* la integración continua;
* el despliegue controlado.

Estos objetivos pueden alcanzarse con un monolito modular.

## 6. Criterio sobre arquitectura hexagonal

No se adoptará una arquitectura hexagonal estricta como objetivo inicial.

Se evitará introducir capas, interfaces y adaptadores si no aportan un beneficio claro en ese momento del curso.

Sin embargo, sí se mantendrán algunas ideas compatibles con una buena arquitectura:

* separar controladores, servicios y repositorios;
* evitar lógica de negocio compleja en los controladores;
* usar DTOs para entrada y salida de datos;
* mantener paquetes cohesionados por funcionalidad;
* evitar acoplamientos innecesarios entre módulos.

El objetivo es que el proyecto sea profesional, pero asumible para alumnado de FP.

## 7. Perfiles de configuración

Más adelante se podrán utilizar perfiles de Spring Boot para separar configuraciones según el entorno.

Perfiles previstos:

```text
dev
test
docker
cloud
```

Uso previsto:

* `dev`: desarrollo desde IntelliJ o Dev Container usando bases de datos dockerizadas publicadas en el equipo local.
* `test`: ejecución de tests automatizados.
* `docker`: ejecución de la aplicación como contenedor dentro de `docker-compose`.
* `cloud`: pruebas futuras contra servicios gestionados en la nube, como AWS RDS o MongoDB Atlas.

Esta separación permitirá evitar configuraciones mezcladas y hará más sencillo cambiar entre entornos.

## 8. Bases de datos dockerizadas

Durante la fase inicial del proyecto, se priorizará el uso de bases de datos dockerizadas.

Esto permite que el profesor y el alumnado trabajen con un entorno reproducible y controlado, sin depender de instalaciones locales de PostgreSQL, MongoDB o Redis.

En desarrollo, la aplicación podrá ejecutarse desde IntelliJ o desde el Dev Container, conectándose a servicios levantados con Docker Compose.

Más adelante se podrán hacer pruebas puntuales con bases de datos en la nube, como AWS RDS o MongoDB Atlas, pero no serán necesarias para el desarrollo ordinario del proyecto.

## 9. Puertos y nombres de servicios

Cuando la aplicación se ejecuta desde el equipo local o desde el Dev Container, se conecta a las bases de datos mediante puertos publicados en el host.

Ejemplo:

```text
localhost:5444
localhost:27018
localhost:6379
```

Cuando la aplicación se ejecute como contenedor dentro de Docker Compose, podrá conectarse a los servicios usando sus nombres internos:

```text
postgres:5432
mongo:27017
redis:6379
```

Por este motivo, los perfiles de configuración serán útiles para evitar mezclar direcciones pensadas para ejecución local con direcciones pensadas para ejecución dentro de Docker.

## 10. Datos iniciales desde fichero

El proyecto mantendrá el uso de un fichero de datos iniciales, como `datos_iniciales.json`.

Esta decisión tiene valor didáctico porque permite trabajar contenidos relacionados con el acceso a ficheros dentro de un proyecto real.

El uso de datos iniciales permite practicar:

* lectura de ficheros;
* procesamiento de JSON;
* mapeo de datos a objetos Java;
* validación básica de datos;
* carga inicial en base de datos;
* integración entre ficheros y persistencia.

Aunque más adelante pueda controlarse cuándo se ejecuta la carga inicial mediante perfiles o propiedades, no se eliminará este mecanismo porque ayuda a trabajar contenidos del módulo de Acceso a Datos.

## 11. Criterio de evolución

GameVault evolucionará mediante mejoras pequeñas y controladas.

Cada mejora debería seguir el flujo:

```text
issue → rama → commit → push → pull request → CI → merge → tag
```

Las nuevas funcionalidades se introducirán cuando la base del proyecto esté suficientemente preparada.

Orden aproximado de evolución:

1. mejorar errores de validación;
2. reforzar tests;
3. revisar excepciones de negocio;
4. introducir perfiles de configuración;
5. preparar seguridad básica;
6. añadir JWT;
7. documentar API con OpenAPI;
8. añadir caché y eventos;
9. trabajar RabbitMQ;
10. valorar pruebas con servicios cloud.

## 12. Principio general

La arquitectura de GameVault debe ser suficientemente profesional para ser útil y defendible, pero no tan compleja como para ocultar los objetivos didácticos.

El proyecto no busca demostrar la arquitectura más sofisticada posible, sino construir una base progresiva, mantenible y comprensible para aprender desarrollo backend moderno.
