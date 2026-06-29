# Dev Container de GameVault

## Objetivo

Este documento explica la primera configuración de Dev Container utilizada en GameVault.

El objetivo es disponer de un entorno de desarrollo reproducible para el proyecto, especialmente útil cuando se trabaja con distintas versiones de Java, Maven, Docker y GitHub CLI.

## Qué problema resuelve

En un proyecto de aula es habitual que cada equipo tenga una configuración distinta:

* versiones diferentes de Java;
* Maven instalado o no instalado;
* Docker configurado de forma diferente;
* problemas para ejecutar el proyecto en otro ordenador;
* diferencias entre el entorno del profesor y el del alumnado.

El Dev Container permite describir el entorno de desarrollo dentro del propio repositorio.

## Qué incluye esta primera versión

La configuración inicial incluye:

* una imagen base Debian;
* Java 25;
* uso del Maven Wrapper del proyecto;
* acceso al Docker del equipo anfitrión;
* GitHub CLI;
* puertos habituales del proyecto reenviados al equipo local.

## Fichero principal

La configuración se encuentra en:

```text
.devcontainer/devcontainer.json
```

## Decisión tomada

En esta primera versión no se ha creado un contenedor de aplicación completo con Docker Compose. El objetivo es empezar con una configuración sencilla que permita validar el flujo de trabajo y abrir el proyecto en un entorno reproducible.

Más adelante se podrá evolucionar esta configuración para integrarla mejor con PostgreSQL, MongoDB, Redis, RabbitMQ o Keycloak.

## Comprobaciones básicas

Una vez abierto el proyecto dentro del Dev Container, se pueden comprobar las herramientas principales:

```bash
java -version
./mvnw -v
docker version
gh --version
```

Si estos comandos funcionan, el entorno base está correctamente preparado.
