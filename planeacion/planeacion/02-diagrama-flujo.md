# DIAGRAMA DE FLUJO

## Sistema SST-Registro - Flujo Principal

---

## Leyenda

```
[ ] = Proceso / Acción
( ) = Decisión
{ } = Inicio/Fin
```

---

## 1. Flujo General - Registro de Accidente/Incidente

```
{INICIO}
    |
    v
[Usuario inicia sesión]
    |
    v
[Verificar credenciales] --> (¿Válido?) --> NO --> [Mostrar error] --> {FIN}
    |
    SI
    v
[Mostrar menú principal]
    |
    +--> [Registrar Evento]
    +--> [Consultar Eventos]
    +--> [Investigar Evento]
    +--> [Ver Estadísticas]
    +--> [Generar Reportes]
    +--> [Gestionar Usuarios] (solo admin)
    +--> [Cerrar sesión]
    |
    v
{SALIDA}
```

---

## 2. Flujo: Registrar Nuevo Evento

```
{INICIO - Registrar Evento}
    |
    v
[Seleccionar tipo: Accidente / Incidente]
    |
    v
[Ingresar datos del evento]
    |
    +-- Fecha y hora
    +-- Área/Lugar
    +-- Personas involucradas
    +-- Descripción del hecho
    +-- Consecuencias
    |
    v
[Seleccionar clasificación]
    |
    +-- Tipo: Accidente / Incidente / Enfermedad profesional
    +-- Gravedad: Leve / Grave / Mortal
    |
    v
[Seleccionar estado]
    |
    +-- Estado inicial: "Abierto"
    +-- Asignar responsable de seguimiento
    |
    v
[Guardar evento]
    |
    v
{Mostrar mensaje: "Registro exitoso"}
    |
    v
{Volver al menú principal}
```

---

## 3. Flujo: Investigar Evento

```
{INICIO - Investigar Evento}
    |
    v
[Buscar evento por código o fecha]
    |
    v
[Seleccionar evento de la lista]
    |
    v
(¿Evento ya investigado?) --> SI --> [Mostrar datos existentes] --> {FIN}
    |
    NO
    v
[Completar formulario de investigación]
    |
    +-- Causa inmediata
    +-- Causas básicas
    +-- Factores contributorios
    +-- Descripción del análisis
    |
    v
[Proponer acciones correctivas]
    |
    +-- Acción 1 + Responsable + Fecha límite
    +-- Acción 2 + Responsable + Fecha límite
    +-- (Agregar más si es necesario)
    |
    v
[Guardar investigación]
    |
    v
[Cambiar estado a "En proceso"]
    |
    v
{Mostrar mensaje: "Investigación guardada"}
    |
    v
{Volver al menú principal}
```

---

## 4. Flujo: Cerrar Caso

```
{INICIO - Cerrar Caso}
    |
    v
[Buscar evento]
    |
    v
[Seleccionar evento]
    |
    v
(¿Tiene acciones pendientes?) --> SI --> [Completar acciones pendientes primero]
    |                                     |
    |                                     v
    NO                                    (Vuelve a verificar)
    |
    v
[Verificar que todas las acciones estén completadas]
    |
    v
(¿Todas completadas?) --> NO --> [No se puede cerrar] --> {FIN}
    |
    SI
    v
[Registrar fecha de cierre]
    |
    v
[Cambiar estado a "Cerrado"]
    |
    v
[Guardar]
    |
    v
{Mostrar mensaje: "Caso cerrado exitosamente"}
    |
    v
{Volver al menú}
```

---

## 5. Flujo: Ver Estadísticas

```
{INICIO - Estadísticas}
    |
    v
[Seleccionar período de tiempo]
    |
    +-- Este mes
    +-- Último trimestre
    +-- Este año
    +-- Rango personalizado
    |
    v
[Generar gráficos]
    |
    +-- Accidentes por mes (barras)
    +-- Accidentes por área (pastel)
    +-- Accidentes por tipo (pastel)
    +-- Accidentes por gravedad (barras)
    |
    v
[Mostrar Dashboard con gráficos]
    |
    v
{Opcional: Exportar a PDF}
    |
    v
{Volver al menú}
```

---

## 6. Modelo de Datos (Diagrama Entidad-Relación Simplificado)

```
+------------------+       +-------------------+
|     USUARIO      |       |      EVENTO       |
+------------------+       +-------------------+
| id_usuario (PK)  |       | id_evento (PK)    |
| nombre           |<------+ id_usuario (FK)  |
| correo           |       | tipo              |
| password         |       | gravedad           |
| rol              |       | estado             |
| area             |       | fecha_hora         |
+------------------+       | lugar              |
        |                  | descripcion        |
        |                  | consecuencias      |
        |                  +-------------------+
        |                            |
        |                            |
        v                            v
+------------------+       +-------------------+
|   INVESTIGACION  |       | ACCION_CORRECTIVA |
+------------------+       +-------------------+
| id_investigacion |<--+   | id_accion (PK)    |
| id_evento (FK)   |   |   | id_investigacion  |
| causa_inmediata  |   +---+ id_evento (FK)    |
| causa_basica     |       | descripcion        |
| factores         |       | responsable        |
| fecha_investigac |       | fecha_limite      |
+------------------+       | estado_accion     |
                            | fecha_completado  |
                            +-------------------+
```

---

## 7. Estados del Evento

```
    +-----------+
    | ABIERTO   |
    +-----------+
         |
         v
    +-----------+     +------------+
    | EN PROCESO|<----|  (puede    |
    +-----------+     |  volver)   |
         |            +------------+
         |
         v
    +---------+
    | CERRADO |
    +---------+
```

---

*Diagrama de flujo - SST-Registro*
