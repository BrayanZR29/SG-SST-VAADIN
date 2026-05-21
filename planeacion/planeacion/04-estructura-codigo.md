# ESTRUCTURA DEL SOFTWARE

## SG-SST - Arquitectura y Paquetes

---

## 1. Arquitectura General

```
+-------------------+-------------------+
| PRESENTATION LAYER                     |
| (CLI - Interfaz Linea de Comandos)   |
+--------+--------+--------+-----------+
| Main.java (Menu CLI)                |
+-------+--------+--------+-----------+
| SERVICE LAYER                        |
+--------+--------+--------+-----------+
| UsuarioServicio | EventoServicio     |
| InvestigacionServicio              |
+-------+--------+--------+-----------+
| DAO LAYER                            |
+--------+--------+--------+-----------+
| UsuarioDao | EventoDao | Investig.  |
| AccionCorrectivaDao                 |
+-------+--------+--------+-----------+
| MODELO (ENTIDADES)                    |
+--------+--------+--------+-----------+
| Usuario | Evento | Investigacion    |
| AccionCorrectiva + Enums            |
+-------+--------+--------+-----------+
| UTILITARIOS                         |
+--------+--------+--------+-----------+
| PoolConexiones | CargadorPropiedades |
+-------+--------+--------+-----------+
| DATABASE                           |
| PostgreSQL + JDBC                  |
+-------------------+-------------------+
```

---

## 2. Estructura de Paquetes

```
src/main/java/com/ssst/
|
+-- Main.java                    <-- Clase principal (main)
|
+-- modelo/
|   +-- Usuario.java
|   +-- Evento.java
|   +-- Investigacion.java
|   +-- AccionCorrectiva.java
|   +-- RolUsuario.java        (enum)
|   +-- TipoEvento.java       (enum)
|   +-- Gravedad.java          (enum)
|   +-- EstadoEvento.java    (enum)
|   +-- EstadoInvestigacion.java (enum)
|   +-- EstadoAccionCorrectiva.java (enum)
|
+-- dao/
|   +-- DaoBase.java          <-- Clase abstracta base
|   +-- UsuarioDao.java
|   +-- EventoDao.java
|   +-- InvestigacionDao.java
|   +-- AccionCorrectivaDao.java
|
+-- servicio/
|   +-- UsuarioServicio.java
|   +-- EventoServicio.java
|   +-- InvestigacionServicio.java
|
+-- util/
    +-- PoolConexiones.java   <-- HikariCP
    +-- CargadorPropiedades.java
```

---

## 3. Estructura de Recursos

```
src/main/resources/
|
+-- application.properties
+-- logback.xml
+-- schema.sql            <-- Script SQL PostgreSQL
```

---

## 4. Build y Dependencias (Maven)

```
pom.xml
|
+-- postgresql         (driver JDBC)
+-- HikariCP          (pool de conexiones)
+-- slf4j-api         (API de logging)
+-- logback-classic   (implementacion)
+-- junit-jupiter     (testing)
+-- mockito-core      (testing)
+-- h2               (testing - base de datos en memoria)
```

---

## 5. Diagrama de Clases Principal

```
+-------------+       +-------------+
| USUARIO     |       | EVENTO      |
+-------------+       +-------------+
| id          |       | id          |
| nombreUsu.. |       | fechaHora   |
| contrasena  |       | lugar       |
| nombreComp.. |       | area        |
| correo      |       | tipo        |
| rol        |       | gravedad    |
| activo     |       | estado      |
| fechaCreac |       | descripcion |
+-------------+       | consecuencias|
       |             +------+------+
       |                    |
       +--------->+--------+
                        |
                  +-------------+
                  | INVESTIGAC. |
                  +-------------+
                  | id           |
                  | eventoId     |
                  | responsable  |
                  | causaInmed  |
                  | causaBasica  |
                  | estado      |
                  +------+------+
                        |
                  +-------------+
                  | ACCION_CORR |
                  +-------------+
                  | id         |
                  | descripcion|
                  | responsable|
                  | fechaPlazo |
                  | estado     |
                  +-------------+
```

---

## 6. Flujo de Ejecucion

```
1. Main.main()
2.    |
3.    +-- PoolConexiones.inicializar()
4.    |
5.    +-- Mostrar menu principal
6.    |
7.    +-- (si autenticado)
8.    |    |-- MenuUsuario
9.    |    |
10.   |    +-- EventoServicio.registrar()
11.   |    |    |-- EventoDao.insertar()
12.   |    |    +-- PoolConexiones.obtenerConexion()
13.   |    |
14.   |    +-- EventoServicio.obtenerTodos()
15.   |    |    |-- EventoDao.seleccionarTodos()
16.   |    |
17.   |    +-- EventoServicio.obtenerEstadisticasPorTipo()
18.   |
19.   +-- PoolConexiones.cerrar()
```

---

## 7. Modelo de Datos (PostgreSQL)

### Tabla: usuarios

| Campo          | Tipo         | Descripcion       |
|----------------|-------------|------------------|
| id             | SERIAL       | Identificador     |
| nombre_usuario | VARCHAR(50)  | Nombre de usuario|
| contrasena     | VARCHAR(255)| Contrasena       |
| nombre_completo| VARCHAR(100)| Nombre completo |
| correo         | VARCHAR(100)| Email           |
| rol           | VARCHAR(20) | Rol usuario     |
| activo        | BOOLEAN     | Activo          |
| fecha_creacion| TIMESTAMP   | Fecha creacion  |

### Tabla: eventos

| Campo              | Tipo         | Descripcion        |
|--------------------|--------------|--------------------|
| id                 | SERIAL       | Identificador      |
| fecha_hora         | TIMESTAMP    | Fecha y hora      |
| lugar             | VARCHAR(100) | Lugar              |
| area              | VARCHAR(100) | Area               |
| responsable_id    | INTEGER      | Responsable       |
| personas_involuc..| TEXT         | Personas          |
| descripcion       | TEXT         | Descripcion       |
| consecuencias      | TEXT         | Consecuencias      |
| tipo              | VARCHAR(30)  | Tipo evento       |
| gravedad         | VARCHAR(20)  | Gravedad          |
| estado            | VARCHAR(20)  | Estado           |
| fecha_creacion   | TIMESTAMP    | Fecha creacion    |
| fecha_actualizac..| TIMESTAMP    | Fecha actualiz.  |
| usuario_reporta..| INTEGER     | Usuario reporta  |

### Tabla: investigaciones

| Campo              | Tipo       | Descripcion      |
|--------------------|------------|------------------|
| id                 | SERIAL     | Identificador    |
| evento_id         | INTEGER    | FK a evento      |
| responsable_id   | INTEGER    | Responsable     |
| causa_inmediata   | TEXT       | Causa inmediata |
| causa_basica      | TEXT       | Causa basica    |
| acciones_propue..| TEXT       | Acciones        |
| conclusion       | TEXT       | Conclusion     |
| fecha_investigac..| TIMESTAMP  | Fecha           |
| fecha_cierre     | TIMESTAMP  | Fecha cierre   |
| estado           | VARCHAR(20)| Estado         |

### Tabla: acciones_correctivas

| Campo            | Tipo       | Descripcion    |
|------------------|------------|----------------|
| id               | SERIAL     | Identificador  |
| investigacion_id | INTEGER   | FK investig.   |
| descripcion     | TEXT      | Descripcion   |
| responsable     | VARCHAR(100)| Responsable   |
| fecha_plazo     | TIMESTAMP | Fecha plazo   |
| fecha_implement..| TIMESTAMP | Fecha impl.    |
| estado          | VARCHAR(20)| Estado       |
| observaciones  | TEXT      | Observaciones |

---

*Estructura del codigo - SG-SST*