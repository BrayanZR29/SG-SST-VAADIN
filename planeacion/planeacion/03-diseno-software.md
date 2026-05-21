# DISEÑO DEL SOFTWARE

## SST-Registro - Diseño de Interfaz CLI

---

## 1. Descripción de la Interfaz

La aplicación será una **interfaz de línea de comandos (CLI)** que se ejecuta en la terminal. El usuario interactúa mediante opciones numeradas y entrada de texto.

---

## 2. Menú Principal (Sin sesión)

```
========================================
  SISTEMA DE GESTION SST - INICIANDO
========================================

========================================
       MENU PRINCIPAL
========================================
1. Iniciar sesion
2. Salir
Seleccione una opcion: _
```

---

## 3. Menú de Usuario (Con sesión)

```
========================================
       MENU USUARIO
========================================
Usuario: Juan Perez
Rol: Responsable SST
----------------------------------------
1. Registrar evento
2. Listar eventos
3. Buscar evento por ID
4. Estadisticas
5. Cerrar sesion
Seleccione una opcion: _
```

---

## 4. Flujo: Registrar Evento

```
--- REGISTRAR EVENTO ---
Tipo (ACCIDENTE/INCIDENTE/ENFERMEDAD_PROFESIONAL): ACCIDENTE
Gravedad (LEVE/GRAVE/MORTAL): LEVE
Descripcion: Caida por resbalon en area mojada
Lugar: Pasillo de almacen
Area: Almacen
Personas involucradas: Pedro Ramirez
Consecuencias: Contusion en rodilla derecha
Evento registrado con ID: 1
```

---

## 5. Flujo: Listar Eventos

```
--- LISTA DE EVENTOS ---
ID: 1 | Tipo: ACCIDENTE | Estado: ABIERTO | Fecha: 2026-04-24T10:30
   Descripcion: Caida por resbalon en area mojada
----------------------------------------
ID: 2 | Tipo: INCIDENTE | Estado: CERRADO | Fecha: 2026-04-23T14:00
   Descripcion: Near miss con carretilla
----------------------------------------
```

---

## 6. Flujo: Buscar Evento por ID

```
Ingrese ID del evento: 1

--- DETALLE DEL EVENTO ---
ID: 1
Tipo: ACCIDENTE
Gravedad: LEVE
Estado: ABIERTO
Fecha: 2026-04-24T10:30
Lugar: Pasillo de almacen
Area: Almacen
Descripcion: Caida por resbalon en area mojada
Consecuencias: Contusion en rodilla derecha
```

---

## 7. Flujo: Estadisticas

```
--- ESTADISTICAS ---
Por tipo:
  Accidente: 5
  Incidente: 12
  Enfermedad Profesional: 1
Por estado:
  Abierto: 3
  En Proceso: 2
  Cerrado: 13
```

---

## 8. Flujo: Iniciar Sesion

```
Nombre de usuario: admin
Contrasena: ********
Bienvenido Administrador del Sistema
```

---

## 9. Casos de Error

### Credenciales invalidas
```
Nombre de usuario: admin
Contrasena: wrongpass
Credenciales invalidas
```

### Evento no encontrado
```
Ingrese ID del evento: 999
Evento no encontrado
```

### Validacion de datos
```
Tipo (ACCIDENTE/INCIDENTE/ENFERMEDAD_PROFESIONAL): INVALIDO
Error: Tipo de evento no valido
```

---

## 10. Convenciones de Interfaz

| Elemento | Formato |
|----------|---------|
| Titulos de menu | `=== TITULO ===` |
| Campos | `Campo: valor` |
| Listas | `ID: valor \| Campo: valor` |
| Separadores | `--- titulo ---` |
| Errores | `Error: mensaje` |

---

*Diseño de interfaz - SST-Registro CLI*