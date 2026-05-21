# Diagramas UML - SST-Registro

Carpeta que contiene los diagramas UML del proyecto.

---

## Archivos Markdown (Descripción textual)

| Archivo | Descripción |
|---------|-------------|
| `01-casos-uso.md` | Diagrama de Casos de Uso - Actores y funcionalidades |
| `02-diagrama-clases.md` | Diagrama de Clases - Entidades y relaciones |
| `03-diagrama-secuencias.md` | Diagrama de Secuencias - Flujos de interacción |
| `04-diagrama-actividades.md` | Diagrama de Actividades - Procesos del sistema |

---

## Archivos PlantUML (Código para generar diagramas)

| Archivo | Descripción |
|---------|-------------|
| `01-casos-uso.puml` | Casos de Uso en PlantUML |
| `02-diagrama-clases.puml` | Diagrama de Clases en PlantUML |
| `03-secuencia-login.puml` | Secuencia: Login |
| `03-secuencia-registrar.puml` | Secuencia: Registrar Evento |
| `03-secuencia-investigacion.puml` | Secuencia: Registrar Investigación |
| `03-secuencia-cerrar.puml` | Secuencia: Cerrar Caso |
| `04-actividad-registro.puml` | Actividad: Registrar Evento |
| `04-actividad-investigacion.puml` | Actividad: Investigar Evento |
| `04-actividad-cerrar.puml` | Actividad: Cerrar Caso |
| `05-diagrama-componentes.puml` | Diagrama de Componentes |

---

## Herramientas para Visualizar PlantUML

### Online
- **PlantUML Online Editor**: https://www.plantuml.com/plantuml/uml/
- **PlantText**: http://www.planttext.com/
- **Kroki**: https://kroki.io/

### Extensiones VS Code
- PlantUML (extensión de Jade Tetreault)

### IntelliJ IDEA
- Plugin PlantUML Integration

### Comando Local
```bash
# Instalar Graphviz (requerido)
sudo apt install graphviz

# Generar imagen desde archivo .puml
plantuml archivo.puml
```

---

## Siguiente Paso

Crear la carpeta `diseño-web/` con la presentación HTML/CSS/JS.
