# PROPUESTA DE SOFTWARE

## "SST-Registro" - Sistema de Gestión de Accidentes e Incidentes Laborales

---

## 1. Nombre del Software

**SST-Registro**

---

## 2. Problema que resuelve

En muchas empresas, el registro de accidentes e incidentes laborales se realiza en papel o en hojas de Excel desorganizadas. Esto dificulta:

- Hacer seguimiento de los casos
- Extraer estadísticas confiables
- Tomar decisiones preventivas a tiempo
- Cumplir con los requisitos legales de reporte

Nuestro software centraliza toda esa información en un solo lugar, accesible y fácil de usar.

---

## 3. ¿Qué hará el software?

Este sistema permitirá a los usuarios:

### Registro
- Registrar accidentes o incidentes de trabajo indicando:
  - Fecha y hora del evento
  - Lugar/área donde ocurrió
  - Personas involucradas
  - Descripción detallada del hecho
  - Consecuencias (lesiones, daños materiales)

### Clasificación
- Clasificar cada evento según:
  - **Tipo**: Accidente, Incidente, Enfermedad profesional
  - **Gravedad**: Leve, Grave, Mortal

### Investigación
- Registrar investigaciones asignando responsables
- Documentar las causas encontradas (causa inmediata, causa básica)
- Proponer acciones correctivas

### Seguimiento
- Monitorear el estado de cada caso (Abierto, En proceso, Cerrado)
- Registrar las acciones correctivas implementadas
- Verificar el cierre de casos

### Estadísticas y Reportes
- Ver gráficos: accidentes por mes, por área, por tipo
- Generar reportes para auditorías
- Exportar información en PDF

---

## 4. ¿A quién va dirigido?

| Rol | Permisos |
|-----|----------|
| Administrador | Configurar el sistema, gestionar usuarios |
| Responsable SST | Gestionar todos los registros, generar reportes |
| Jefe de Área | Ver y reportar incidentes de su área |
| Trabajador | Reportar incidentes de forma anónima o identificada |

---

## 5. Beneficios esperados

- ✅ Reducir el tiempo de registro de accidentes
- ✅ Tener información organizada para auditorías
- ✅ Tomar decisiones preventivas basadas en datos reales
- ✅ Cumplir con la normativa legal vigente (Ley 29783 en Perú)
- ✅ Acceso rápido a la información desde cualquier computadora

---

## 6. Alcance del proyecto

### Módulos incluidos
1. **Autenticación y usuarios** - Login, roles
2. **Registro de eventos** - CRUD completo
3. **Clasificación y severidad** - Catálogos predefinidos
4. **Investigación** - Formulario de causas y acciones
5. **Seguimiento** - Estados y cierre de casos
6. **Dashboard** - Estadísticas y gráficos básicos
7. **Reportes** - Generación de informes

### Módulos fuera de alcance (para versiones futuras)
- Notificaciones por correo automático
- App móvil
- Integración con sistemas externos

---

## 7. Tecnologías a usar

| Componente | Tecnología |
|------------|------------|
| Backend | Java 17 Puro (POO) |
| Acceso a datos | JDBC + HikariCP |
| Base de datos | PostgreSQL |
| Logging | SLF4J + Logback |
| Build | Maven (pom.xml) |
| Testing | JUnit 5 + Mockito |
| Control de versiones | Git |

---

## 8. Limitaciones reconocidas

- Interfaz CLI (línea de comandos)
- Base de datos local (no cloud)
- Un idioma (español)

---

*Propuesta elaborada para trabajo de clase - Seguridad y Salud en el Trabajo*
