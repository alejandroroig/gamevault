# Auditoría técnica inicial de GameVault

## 1. Objetivo

## 2. Entorno verificado

## 3. Resultado de compilación y tests

## 4. Estructura actual del proyecto

## 5. Dependencias principales

## 6. Servicios Docker actuales

## 7. Incidencias detectadas

### 7.1 IntelliJ no detecta Git dentro del Dev Container
IntelliJ puede abrir el terminal dentro del contenedor, pero seguir intentando usar Git local de Windows.

### 7.2 JDK no especificado en IntelliJ
IntelliJ puede no asociar automáticamente el JDK del contenedor al proyecto.

### 7.3 Error de permisos en target/
Maven puede fallar si target/ fue generado fuera del contenedor o con permisos incompatibles.

Como solución rápida, se puede limpiar el directorio target/ y recompilar dentro del contenedor.

```bash
rm -rf target
./mvnw test
```


## 8. Deuda técnica inicial

## 9. Próximos pasos recomendados