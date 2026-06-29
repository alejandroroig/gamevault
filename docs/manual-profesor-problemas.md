# Manual del profesor ante problemas frecuentes

Este documento recoge incidencias reales detectadas durante la preparación del proyecto GameVault y posibles soluciones rápidas.

El objetivo no es explicar todos los detalles técnicos en profundidad, sino ofrecer una guía práctica para poder desbloquear una sesión de aula o una práctica de laboratorio.

## 1. Comprobaciones básicas del entorno

Cuando algo falle dentro del Dev Container, conviene empezar comprobando dónde estamos y qué herramientas están disponibles.

```bash
pwd
uname -a
whoami
java -version
./mvnw -v
git --version
gh --version
docker version
```

Resultados esperados dentro del Dev Container:

* `pwd` debería mostrar una ruta Linux, por ejemplo `/IdeaProjects/gamevault`.
* `uname -a` debería indicar Linux.
* `whoami` debería devolver `vscode`.
* `java -version` debería mostrar Java 25.
* `./mvnw -v` debería funcionar usando el Maven Wrapper.
* `git --version`, `gh --version` y `docker version` deberían responder correctamente.

## 2. El terminal parece “local”, pero realmente está dentro del contenedor

En IntelliJ puede aparecer una pestaña de terminal llamada `Local`, aunque el terminal esté realmente dentro del Dev Container.

Para comprobarlo:

```bash
pwd
uname -a
whoami
```

Si aparecen rutas Linux como `/IdeaProjects/gamevault` y el usuario es `vscode`, el terminal está dentro del contenedor.

No hay que fiarse solo del nombre visual de la pestaña del terminal.

## 3. IntelliJ muestra todo en rojo y dice que falta el JDK

Síntoma habitual:

```text
JDK "openjdk-25" is missing
```

O bien el proyecto aparece con muchos errores rojos aunque Maven compile correctamente desde terminal.

Solución:

1. Ir a `File → Project Structure`.
2. Entrar en `Project`.
3. Seleccionar o autodetectar el JDK del Dev Container.
4. Usar una ruta como:

```text
/usr/local/sdkman/candidates/java/current
```

o una ruta concreta similar a:

```text
/usr/local/sdkman/candidates/java/25.0.3-tem
```

Después conviene recargar el proyecto Maven desde IntelliJ.

## 4. IntelliJ no detecta Git dentro del Dev Container

Puede ocurrir que IntelliJ intente usar una ruta de Git de Windows, por ejemplo:

```text
C:\Program Files\Git\bin\git.exe
```

Dentro del contenedor esa ruta no existe.

Comprobación desde terminal:

```bash
which git
git --version
```

Si Git funciona desde terminal, se puede seguir trabajando con Git desde el terminal del Dev Container aunque la integración gráfica de IntelliJ no funcione correctamente.

Comandos habituales:

```bash
git status
git diff
git add .
git commit -m "mensaje"
git push
```

## 5. Error de permisos con la carpeta target

Síntoma:

```text
Operation not permitted
target/classes/application.yaml
```

Este problema puede aparecer si la carpeta `target/` fue generada previamente desde otro entorno, por ejemplo Windows, IntelliJ local o una sesión anterior del Dev Container.

Solución rápida:

```bash
rm -rf target
./mvnw test
```

Si no permite borrar la carpeta:

```bash
sudo rm -rf target
./mvnw test
```

Este problema no significa necesariamente que los tests fallen. Puede ser simplemente un problema de permisos o de archivos generados en otro entorno.

## 6. GitHub no permite hacer push con usuario y contraseña

Síntoma:

```text
Password authentication is not supported for Git operations.
```

GitHub no permite usar la contraseña normal de la cuenta para hacer operaciones Git por HTTPS.

Solución recomendada dentro del Dev Container:

```bash
gh auth login
gh auth setup-git
```

Después se puede repetir:

```bash
git push
```

Si hay varias cuentas configuradas:

```bash
gh auth status
gh auth switch --user USUARIO
gh auth setup-git
```

## 7. No se puede crear una Pull Request

Síntoma:

```text
No commits between master and nombre-rama
Head ref must be a branch
```

Causas habituales:

* no se ha hecho commit;
* se ha hecho commit local, pero no se ha hecho push;
* la rama local no existe en GitHub.

Comprobaciones:

```bash
git status
git log --oneline --decorate -5
```

Solución habitual:

```bash
git push -u origin nombre-rama
```

Después:

```bash
gh pr create
```

## 8. El CI falla en GitHub Actions

Si el workflow de CI falla, no se debe fusionar la Pull Request sin leer el log.

Comprobaciones iniciales:

1. Entrar en la Pull Request.
2. Abrir la pestaña `Checks` o `Actions`.
3. Revisar el paso que aparece en rojo.
4. Comparar con la ejecución local:

```bash
rm -rf target
./mvnw -B test
```

Si local funciona y GitHub falla, revisar diferencias de entorno, versión de Java, permisos del Maven Wrapper o dependencias externas.

## 9. Comandos rápidos de recuperación

Limpiar compilación:

```bash
rm -rf target
./mvnw test
```

Ver rama actual:

```bash
git branch
git status
```

Actualizar `master`:

```bash
git checkout master
git pull origin master
```

Crear rama nueva:

```bash
git checkout -b nombre-rama
```

Ver PRs e issues:

```bash
gh issue list --state all
gh pr list --state all
```

Comprobar autenticación con GitHub:

```bash
gh auth status
```

## 10. Recomendación didáctica

Si este proyecto se inicia desde cero en un curso, conviene crear el Dev Container al principio, antes de generar muchos archivos desde distintos entornos.

Esto reduce problemas de:

* versiones distintas de Java;
* Maven instalado o no instalado;
* permisos entre Windows, WSL2 y Docker;
* carpetas `target/` generadas desde entornos diferentes;
* diferencias entre el equipo del profesor y el del alumnado.
