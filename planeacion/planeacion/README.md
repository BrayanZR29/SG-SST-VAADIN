# SG-SST

## Sistema de Gestión de Accidentes e Incidentes Laborales

---

## 📋 Descripción

Sistema de gestión de accidentes e incidentes laborales con interfaz de línea de comandos (CLI), diseñado para cumplir con los estándares de Seguridad y Salud en el Trabajo (SST).

---

## 📁 Estructura del Proyecto

```
SG-SST/
│
├── README.md                      ← Índice principal
│
├── pom.xml                       ← Configuración Maven
│
├── src/
│   ├── main/
│   │   ├── java/com/ssst/
│   │   │   ├── Main.java                    ← Clase principal (main)
│   │   │   │
│   │   │   ├── modelo/                     ← Entidades
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Evento.java
│   │   │   │   ├── Investigacion.java
│   │   │   │   ├── AccionCorrectiva.java
│   │   │   │   └── enums/
│   │   │   │
│   │   │   ├── dao/                        ← Acceso a datos (JDBC)
│   │   │   │   ├── DaoBase.java
│   │   │   │   ├── UsuarioDao.java
│   │   │   │   ├── EventoDao.java
│   │   │   │   └── InvestigacionDao.java
│   │   │   │
│   │   │   ├── servicio/                   ← Lógica de negocio
│   │   │   │   ├── UsuarioServicio.java
│   │   │   │   ├── EventoServicio.java
│   │   │   │   └── InvestigacionServicio.java
│   │   │   │
│   │   │   └── util/
│   │   │       ├── PoolConexiones.java
│   │   │       └── CargadorPropiedades.java
│   │   │
│   │   └── resources/
│   │       ├── logback.xml
│   │       ├── schema.sql
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/ssst/
│           ├── dao/
│           │   ├── UsuarioDaoTest.java
│           │   └── EventoDaoTest.java
│           └── servicio/
│               ├── UsuarioServicioTest.java
│               └── EventoServicioTest.java
│
└── planeacion/
    ├── 01-propuesta-software.md
    ├── 02-diagrama-flujo.md
    ├── 03-diseno-software.md
    └── 04-estructura-codigo.md
```

---

## 🚀 Funcionalidades Principales

- ✅ Registro de accidentes e incidentes
- ✅ Clasificación por tipo y gravedad
- ✅ Investigación de eventos
- ✅ Seguimiento de acciones correctivas
- ✅ Estadísticas
- ✅ Gestión de usuarios con roles
- ✅ Cumplimiento de normativa legal (Ley 29783)

---

## 🛠️ Tecnologías

| Componente | Tecnología |
|------------|------------|
| Backend | Java 17 Puro (POO) |
| Acceso a datos | JDBC + HikariCP |
| Base de datos | PostgreSQL |
| Logging | SLF4J + Logback |
| Build | Maven |
| Testing | JUnit 5 + Mockito |

---

## 📋 Cómo Ejecutar

### 1. Requisitos Previos

- Java 17
- Maven
- PostgreSQL

### 2. Crear Base de Datos

```sql
CREATE DATABASE ssst;
```

### 3. Ejecutar Script SQL

```bash
psql -U postgres -d ssst -f src/main/resources/schema.sql
```

### 4. Compilar y Ejecutar

```bash
mvn compile
mvn exec:java
```

### 5. Ejecutar Tests

```bash
mvn test
```

---

## 👤 Usuarios por Defecto

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| admin | admin123 | Administrador |
| responsable | resp123 | Responsable SST |

---

## 📅 Estados del Proyecto

| Fase | Estado |
|------|--------|
| Propuesta de software | ✅ Completado |
| Diagramas de flujo (texto) | ✅ Completado |
| Diseño de CLI | ✅ Completado |
| Estructura del código | ✅ Completado |
| Implementación código | ✅ Completado |
| Pruebas unitarias | ✅ Completado |
| Documentación | ✅ Completado |

---

*Trabajo de clase - Seguridad y Salud en el Trabajo*