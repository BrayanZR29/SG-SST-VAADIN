# DIAGRAMA DE CASOS DE USO
## SST-Registro - Casos de Uso

---

## Actores

| Actor | Descripción |
|-------|-------------|
| 🧑‍💼 Trabajador | Puede reportar incidentes y ver su propio historial |
| 👷 Jefe de Área | Gestiona incidentes de su área |
| 🔬 Responsable SST | Gestiona todo el sistema de incidentes |
| 👨‍💻 Administrador | Administra usuarios y configuración |

---

## Diagrama de Casos de Uso

```
                    ┌─────────────────────────────────────────────────────────┐
                    │                    SISTEMA SST-REGISTRO                 │
                    │                                                        │
                    │                                                        │
  ┌─────────────┐   │   ┌──────────────────┐    ┌──────────────────────────┐│
  │  TRABAJADOR │   │   │   UC-1: Login     │    │  UC-2: Cerrar Sesión    ││
  └─────────────┘   │   │   · Ingresar      │    │  · Finalizar sesión      ││
        │          │   │     credenciales   │    └──────────────────────────┘│
        │          │   └────────┬───────────┘                                │
        │          │            │                                             │
        │          │            ▼                                             │
        │          │   ┌──────────────────┐    ┌──────────────────────────┐│
        │          │   │   UC-3: Registrar│    │  UC-4: Ver Dashboard     ││
        │          │   │   Incidente      │    │  · Ver resumen          ││
        │          │   │   · Tipo         │    │  · Ver últimos eventos  ││
        │          │   │   · Descripción  │    └──────────────────────────┘│
        │          │   │   · Ubicación    │                                │
        │          │   └────────┬───────────┘                                │
        │          │            │                                             │
        │          │            ▼                                             │
        │          │   ┌──────────────────┐    ┌──────────────────────────┐│
        │          │   │   UC-5: Consultar│    │  UC-6: Ver Detalle       ││
        │          │   │   Eventos        │    │  Evento                  ││
        │          │   │   · Buscar       │    │  · Ver información       ││
        │          │   │   · Filtrar      │    │  · Ver investigación     ││
        │          │   │   · Paginar      │    │  · Ver acciones          ││
        │          │   └────────┬───────────┘    └──────────────────────────┘│
        │          │            │                                             │
        │          │            ▼                                             │
        │          │   ┌──────────────────┐    ┌──────────────────────────┐│
        │          │   │   UC-7: Registrar│    │  UC-8: Gestionar         ││
        │          │   │   Investigación  │    │  Acciones Correctivas   ││
        │          │   │   · Causas       │    │  · Agregar acción       ││
        │          │   │   · Factores     │    │  · Marcar completada    ││
        │          │   │   · Analisis     │    │  · Ver estado           ││
        │          │   └────────┬───────────┘    └──────────────────────────┘│
        │          │            │                                             │
        │          │            ▼                                             │
        │          │   ┌──────────────────┐    ┌──────────────────────────┐│
        │          │   │   UC-9: Cerrar   │    │  UC-10: Ver              ││
        │          │   │   Caso           │    │  Estadísticas            ││
        │          │   │   · Verificar    │    │  · Gráficos              ││
        │          │   │     acciones     │    │  · Filtros por período   ││
        │          │   │   · Cerrar       │    └──────────────────────────┘│
        │          │   └────────┬───────────┘                                │
        │          │            │                                             │
        │          │            ▼                                             │
        │          │   ┌──────────────────┐    ┌──────────────────────────┐│
        │          │   │  UC-11: Generar   │    │  UC-12: Exportar PDF     ││
        │          │   │  Reportes         │    │  · Reporte individual    ││
        │          │   │   · Por período   │    │  · Reporte general       ││
        │          │   │   · Por área      │    └──────────────────────────┘│
        │          │   │   · Por tipo      │                                │
        │          │   └──────────────────┘                                │
        │          │                                                        │
        │          │                                                        │
  ┌─────────────┐   │   ┌──────────────────┐    ┌──────────────────────────┐│
  │ADMINISTRADOR│   │   │  UC-13: Gestionar│    │  UC-14: Configurar       ││
  └─────────────┘   │   │  Usuarios         │    │  Sistema                 ││
        │          │   │  · Crear usuario  │    │  · Áreas                 ││
        │          │   │  · Editar usuario │    │  · Tipos de evento       ││
        │          │   │  · Eliminar       │    └──────────────────────────┘│
        │          │   │  · Asignar rol   │                                │
        │          │   └──────────────────┘                                │
        │          │                                                        │
  ┌─────────────┐   │                                                        │
  │ JEFE ÁREA   │   │                                                        │
  └─────────────┘   │                                                        │
        │          │                                                        │
        │          │                                                        │
  ┌─────────────┐   │                                                        │
  │RESPONSABLE  │   │                                                        │
  │    SST      │   │                                                        │
  └─────────────┘   │                                                        │
                    │                                                        │
                    └─────────────────────────────────────────────────────────┘
```

---

## Relación entre Actores y Casos de Uso

| Actor | Casos de Uso |
|-------|-------------|
| **Trabajador** | UC-1, UC-2, UC-3, UC-4, UC-5, UC-6, UC-12 |
| **Jefe de Área** | UC-1, UC-2, UC-3, UC-4, UC-5, UC-6, UC-7, UC-8, UC-9, UC-10, UC-11, UC-12 |
| **Responsable SST** | Todos los casos de uso |
| **Administrador** | UC-1, UC-2, UC-4, UC-10, UC-11, UC-12, UC-13, UC-14 |

---

## Descripción de Casos de Uso Principales

### UC-1: Login
- **Actor:** Todos
- **Descripción:** El usuario ingresa sus credenciales para acceder al sistema
- **Flujo básico:**
  1. Usuario ingresa correo
  2. Usuario ingresa contraseña
  3. Sistema valida credenciales
  4. Sistema muestra Dashboard

### UC-3: Registrar Incidente
- **Actor:** Trabajador, Jefe de Área, Responsable SST
- **Descripción:** Registrar un nuevo accidente o incidente
- **Flujo básico:**
  1. Usuario selecciona "Registrar Evento"
  2. Sistema muestra formulario
  3. Usuario completa datos obligatorios
  4. Usuario guarda registro
  5. Sistema genera código único

### UC-7: Registrar Investigación
- **Actor:** Jefe de Área, Responsable SST
- **Descripción:** Documentar la investigación de un evento
- **Flujo básico:**
  1. Usuario selecciona evento
  2. Usuario completa análisis de causas
  3. Usuario registra acciones correctivas
  4. Sistema actualiza estado a "En Proceso"

---
