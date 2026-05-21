# DIAGRAMA DE CLASES
## SST-Registro - Modelo de Clases

---

## Paquete: Modelo / Entidades

### Clase: Usuario

```
┌─────────────────────────────────────────────┐
│              <<entity>>                     │
│              Usuario                        │
├─────────────────────────────────────────────┤
│ - id: Long {PK}                             │
│ - nombre: String {not null, length=100}     │
│ - correo: String {unique, not null}        │
│ - password: String {not null, length=255}  │
│ - rol: RolUsuario {not null}               │
│ - area: String {length=100}                │
│ - activo: Boolean = true                   │
│ - fechaCreacion: LocalDateTime             │
├─────────────────────────────────────────────┤
│ + getters()                                 │
│ + setters()                                 │
│ + encryptPassword(): void                  │
│ + getAuthorities(): Collection<?           │
└─────────────────────────────────────────────┘
           │ 1
           │ 1..*
           ▼
```

### Clase: Evento

```
┌─────────────────────────────────────────────┐
│              <<entity>>                     │
│                Evento                       │
├─────────────────────────────────────────────┤
│ - id: Long {PK}                             │
│ - codigo: String {unique, length=20}       │
│ - tipo: TipoEvento {not null}              │
│ - gravedad: Gravedad {not null}           │
│ - estado: EstadoEvento = ABIERTO           │
│ - fechaHora: LocalDateTime {not null}     │
│ - lugar: String {length=100}               │
│ - descripcion: String {columnDefinition}   │
│ - consecuencias: String                    │
│ - personasInvolucradas: String             │
│ - usuarioRegistra: Usuario {many-to-one}   │
│ - fechaCreacion: LocalDateTime             │
│ - fechaActualizacion: LocalDateTime        │
├─────────────────────────────────────────────┤
│ + getters()                                 │
│ + setters()                                 │
│ + agregarInvestigacion(Investigacion)      │
│ + getInvestigacion(): Investigacion         │
│ + generarCodigo(): String                  │
│ + cerrar(): void                           │
└─────────────────────────────────────────────┘
           │
           │ 1
           │
           ▼
```

### Clase: Investigacion

```
┌─────────────────────────────────────────────┐
│              <<entity>>                     │
│            Investigacion                   │
├─────────────────────────────────────────────┤
│ - id: Long {PK}                             │
│ - causaInmediata: String                    │
│ - causaBasica: String                       │
│ - factoresContribuitorios: String          │
│ - fechaInvestigacion: LocalDateTime        │
│ - evento: Evento {one-to-one}              │
│ - investigador: Usuario {many-to-one}       │
├─────────────────────────────────────────────┤
│ + getters()                                 │
│ + setters()                                 │
│ + agregarAccionCorrectiva(AccionCorrectiva)│
│ + getAcciones(): List<AccionCorrectiva>     │
└─────────────────────────────────────────────┘
           │
           │ 1
           │
           ▼
```

### Clase: AccionCorrectiva

```
┌─────────────────────────────────────────────┐
│              <<entity>>                     │
│           AccionCorrectiva                  │
├─────────────────────────────────────────────┤
│ - id: Long {PK}                             │
│ - descripcion: String {length=500}         │
│ - responsable: String {length=100}         │
│ - fechaLimite: LocalDate                   │
│ - estadoAccion: EstadoAccion = PENDIENTE   │
│ - fechaCompletado: LocalDate               │
│ - investigacion: Investigacion {many-to-one}│
├─────────────────────────────────────────────┤
│ + getters()                                 │
│ + setters()                                 │
│ + completar(): void                        │
│ + estaVencada(): boolean                   │
└─────────────────────────────────────────────┘
```

---

## Enumeraciones

### RolUsuario
```
┌─────────────────────────────────────────────┐
│           <<enumeration>>                   │
│              RolUsuario                     │
├─────────────────────────────────────────────┤
│ + ADMIN                                     │
│ + RESPONSABLE_SST                           │
│ + JEFE_AREA                                 │
│ + TRABAJADOR                                │
└─────────────────────────────────────────────┘
```

### TipoEvento
```
┌─────────────────────────────────────────────┐
│           <<enumeration>>                   │
│              TipoEvento                     │
├─────────────────────────────────────────────┤
│ + ACCIDENTE                                  │
│ + INCIDENTE                                  │
│ + ENFERMEDAD_PROFESIONAL                    │
└─────────────────────────────────────────────┘
```

### Gravedad
```
┌─────────────────────────────────────────────┐
│           <<enumeration>>                   │
│                Gravedad                     │
├─────────────────────────────────────────────┤
│ + LEVE                                       │
│ + GRAVE                                      │
│ + MORTAL                                     │
└─────────────────────────────────────────────┘
```

### EstadoEvento
```
┌─────────────────────────────────────────────┐
│           <<enumeration>>                   │
│             EstadoEvento                    │
├─────────────────────────────────────────────┤
│ + ABIERTO                                    │
│ + EN_PROCESO                                 │
│ + CERRADO                                    │
└─────────────────────────────────────────────┘
```

### EstadoAccion
```
┌─────────────────────────────────────────────┐
│           <<enumeration>>                   │
│             EstadoAccion                    │
├─────────────────────────────────────────────┤
│ + PENDIENTE                                  │
│ + COMPLETADA                                │
└─────────────────────────────────────────────┘
```

---

## Relaciones entre Clases

```
┌──────────────┐         ┌──────────────┐         ┌──────────────────┐
│   Usuario    │         │    Evento    │         │  Investigacion   │
├──────────────┤         ├──────────────┤         ├──────────────────┤
│              │ 1     * │              │ 1     1 │                  │
│              │─────────│              │─────────│                  │
└──────────────┘         └──────────────┘         └────────┬─────────┘
                                                            │
                                                            │ 1
                                                            │ 1..*
                                                            ▼
                                                   ┌──────────────────┐
                                                   │ AccionCorrectiva │
                                                   ├──────────────────┤
                                                   │                  │
                                                   │                  │
                                                   └──────────────────┘
```

### Detalle de Relaciones

| Clase A | Relación | Clase B | Multiplicidad |
|---------|----------|---------|---------------|
| Usuario | registra | Evento | 1 → * |
| Evento | tiene | Investigacion | 1 → 1 |
| Investigacion | pertenece | Evento | 1 → 1 |
| Investigacion | tiene | AccionCorrectiva | 1 → * |
| Usuario | investiga | Investigacion | 1 → * |

---

## DTOs (Data Transfer Objects)

### EventoDTO
```
┌─────────────────────────────────────────────┐
│              <<DTO>>                        │
│              EventoDTO                      │
├─────────────────────────────────────────────┤
│ - id: Long                                  │
│ - codigo: String                            │
│ - tipo: String                              │
│ - gravedad: String                          │
│ - estado: String                            │
│ - fechaHora: String                        │
│ - lugar: String                             │
│ - descripcion: String                      │
│ - consecuencias: String                    │
│ - personasInvolucradas: String             │
│ - nombreRegistra: String                   │
│ - tieneInvestigacion: Boolean              │
├─────────────────────────────────────────────┤
│ + getters()                                 │
│ + setters()                                 │
│ + of(Evento): EventoDTO                    │
└─────────────────────────────────────────────┘
```

### EstadisticaDTO
```
┌─────────────────────────────────────────────┐
│              <<DTO>>                        │
│           EstadisticaDTO                   │
├─────────────────────────────────────────────┤
│ - totalAccidentes: Integer                 │
│ - totalIncidentes: Integer                 │
│ - totalEnfermedades: Integer               │
│ - totalLeve: Integer                        │
│ - totalGrave: Integer                       │
│ - totalMortal: Integer                      │
│ - totalAbiertos: Integer                    │
│ - totalCerrados: Integer                    │
│ - porMes: Map<String, Integer>             │
│ - porArea: Map<String, Integer>            │
├─────────────────────────────────────────────┤
│ + getters()                                 │
│ + setters()                                 │
└─────────────────────────────────────────────┘
```

---
