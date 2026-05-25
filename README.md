# InmoSmart - Plataforma Inmobiliaria Digital

Proyecto final de Programacion I - Simulador de plataforma inmobiliaria desarrollado en
Java con JavaFX y JUnit 5.

## Estructura del proyecto

```
InmoSmart/
├── pom.xml
├── src/
│   ├── main/java/
│   │   ├── app/
│   │   │   └── MainApp.java           (Clase principal JavaFX)
│   │   ├── model/                     (Entidades del dominio)
│   │   │   ├── Usuario.java           (Abstracta)
│   │   │   ├── Comprador.java
│   │   │   ├── Vendedor.java
│   │   │   ├── Inmueble.java
│   │   │   ├── Oferta.java
│   │   │   ├── Publicacion.java
│   │   │   ├── Transaccion.java
│   │   │   ├── TipoUsuario.java       (enum)
│   │   │   ├── TipoInmueble.java      (enum)
│   │   │   ├── EstadoInmueble.java    (enum)
│   │   │   ├── EstadoOferta.java      (enum)
│   │   │   ├── TipoOperacion.java     (enum)
│   │   │   └── RangoUsuario.java      (enum)
│   │   ├── interfaces/
│   │   │   └── OperacionesInmobiliarias.java
│   │   ├── service/
│   │   │   └── InmoSmartService.java  (Logica de negocio + metodo burbuja)
│   │   └── ui/
│   │       ├── RegistroUsuarioTab.java
│   │       ├── PublicarInmuebleTab.java
│   │       ├── BuscarInmuebleTab.java
│   │       ├── OfertasTab.java
│   │       ├── TransaccionesTab.java
│   │       ├── ReportesTab.java
│   │       └── AlertasTab.java
│   └── test/java/test/
│       ├── UsuarioTest.java
│       ├── InmuebleTest.java
│       ├── OfertaTest.java
│       └── ServiceTest.java
```

## Como ejecutar

### Opcion 1: Con Maven (recomendado)

Estando en la raiz del proyecto `InmoSmart/`:

```bash
mvn clean javafx:run
```

### Opcion 2: Solo correr las pruebas JUnit

```bash
mvn test
```

### Opcion 3: Con IntelliJ IDEA o NetBeans

1. Abrir el proyecto como proyecto Maven (`pom.xml`).
2. Esperar a que descargue las dependencias (JavaFX y JUnit).
3. Click derecho sobre `MainApp.java` -> Run.

## Funcionalidades implementadas

| # | Funcionalidad | Donde |
|---|---|---|
| 1 | Registro de compradores y vendedores | Pestaña Registro |
| 2 | Publicacion de inmuebles | Pestaña Publicar Inmueble |
| 3 | Busqueda por ciudad, tipo, precio, area | Pestaña Buscar Inmuebles |
| 4 | Realizar ofertas | Pestaña Ofertas |
| 5 | Aceptar/rechazar ofertas | Pestaña Ofertas |
| 6 | Compra y arriendo | Pestaña Transacciones |
| 7 | Reportes generales | Pestaña Reportes |
| 8 | Sistema de reputacion (puntos + rangos) | Visible en Reportes |
| 9 | Recomendaciones simples | Pestaña Buscar Inmuebles |
| 10 | Alertas automaticas | Pestaña Alertas |
| 11 | Validaciones | En toda la app |

## Sistema de puntos / reputacion

| Accion | Puntos |
|---|---|
| Publicar inmueble | +10 (vendedor) |
| Realizar oferta | +5 (comprador) |
| Comprar inmueble | +50 (comprador) |
| Completar transaccion | +100 (ambos) |

### Rangos por puntos:

- **PRINCIPIANTE**: 0 puntos
- **INVERSIONISTA**: 100+ puntos
- **EXPERTO_INMOBILIARIO**: 300+ puntos
- **MAGNATE_INMOBILIARIO**: 600+ puntos

## Metodo burbuja

El metodo burbuja se implementa en `InmoSmartService.java`:

1. **`ordenarInmueblesPorPrecio(ArrayList<Inmueble>)`**: ordena los inmuebles de menor
   a mayor precio. Usado desde la pestaña **Buscar Inmuebles** al hacer click en
   "Ordenar por precio (Burbuja)".

2. **`ordenarUsuariosPorPuntos()`**: ordena todos los usuarios de mayor a menor puntos
   para mostrar el ranking. Usado en la pestaña **Reportes** y para el TOP 3 del reporte.

### Como funciona:

El algoritmo recorre el arreglo `n-1` veces. En cada pasada compara los pares de
elementos adyacentes y si estan en el orden incorrecto los intercambia. Al final
de cada pasada el elemento mayor (o menor segun el orden) queda en su posicion
correcta. Es un algoritmo simple, ideal para listas pequeñas como las del proyecto.

## Tecnologias

- Java 17+
- JavaFX 21
- JUnit 5
- Maven

## Autor

Proyecto academico - Ingenieria de Sistemas - Programacion I - 2026
