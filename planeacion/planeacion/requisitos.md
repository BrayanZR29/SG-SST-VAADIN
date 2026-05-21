# Requisitos del Proyecto SST-Registro

## ¿Qué es esto?

Es un sistema web para registrar y gestionar accidentes e incidentes laborales de forma centralizada.

---

## Funcionalidades que necesita

### 1. Registro de eventos
- Registrar accidentes o incidentes indicando:
  - Fecha y hora
  - Lugar o área donde ocurrió
  - Personas involucradas
  - Qué pasó exactamente (descripción)
  - Qué consecuencias hubo (lesiones, daños materiales)

### 2. Clasificación de eventos
- **Por tipo:** Accidente, Incidente, Enfermedad profesional
- **Por gravedad:** Leve, Grave, Mortal

### 3. Investigación de eventos
- Asignar responsables a cada investigación
- Registrar las causas (causa inmediata y causa básica)
- Proponer acciones correctivas

### 4. Seguimiento de casos
- Estados: Abierto → En proceso → Cerrado
- Registrar las acciones correctivas implementadas
- Verificar que el caso esté cerrado correctamente

### 5. Estadísticas y reportes
- Ver gráficos: cuántos accidentes por mes, por área, por tipo
- Generar reportes para auditorías
- Exportar información en PDF

### 6. Gestión de usuarios
- Diferentes roles con diferentes permisos:
  - **Administrador:** Configura el sistema y gestiona usuarios
  - **Responsable SST:** Gestiona todo y genera reportes
  - **Jefe de Área:** Ve y reporta incidentes de su área
  - **Trabajador:** Reporta incidentes (anónimo o identificado)

---

## Tecnologías que se usan

| Componente | Tecnología |
|------------|------------|
| Backend | Java 17 + Spring Boot |
| Frontend | Thymeleaf + Bootstrap |
| Base de datos | MySQL |
| Seguridad | Spring Security |

---

## Resumen

Básicamente, quieres un sistema donde cualquier trabajador pueda reportar un accidente o incidente, los responsables puedan investigar qué pasó, registrar las causas y las acciones que se van a tomar para que no vuelva a pasar, y todo esto quede registrado para poder hacer estadísticas y cumplir con la normativa legal (Ley 29783 de Perú).

Es un sistema simple pero completo, nada overkill, enfocado en lo esencial.