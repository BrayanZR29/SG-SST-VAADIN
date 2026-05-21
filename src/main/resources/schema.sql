-- ============================================
-- SCRIPT DE BASE DE DATOS - SG-SST
-- Sistema de Gestion de Seguridad y Salud en el Trabajo
-- ============================================

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    correo VARCHAR(100),
    rol VARCHAR(20) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de eventos
CREATE TABLE IF NOT EXISTS eventos (
    id SERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP NOT NULL,
    lugar VARCHAR(100),
    area VARCHAR(100),
    responsable_id INTEGER,
    personas_involucradas TEXT,
    descripcion TEXT NOT NULL,
    consecuencias TEXT,
    tipo VARCHAR(30) NOT NULL,
    gravedad VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTO',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_reporta_id INTEGER,
    FOREIGN KEY (responsable_id) REFERENCES usuarios(id),
    FOREIGN KEY (usuario_reporta_id) REFERENCES usuarios(id)
);

-- Tabla de investigaciones
CREATE TABLE IF NOT EXISTS investigaciones (
    id SERIAL PRIMARY KEY,
    evento_id INTEGER NOT NULL UNIQUE,
    responsable_id INTEGER,
    causa_inmediata TEXT,
    causa_basica TEXT,
    acciones_propuestas TEXT,
    conclusion TEXT,
    fecha_investigacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre TIMESTAMP,
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTA',
    FOREIGN KEY (evento_id) REFERENCES eventos(id),
    FOREIGN KEY (responsable_id) REFERENCES usuarios(id)
);

-- Tabla de acciones correctivas
CREATE TABLE IF NOT EXISTS acciones_correctivas (
    id SERIAL PRIMARY KEY,
    investigacion_id INTEGER NOT NULL,
    descripcion TEXT NOT NULL,
    responsable VARCHAR(100),
    fecha_plazo TIMESTAMP NOT NULL,
    fecha_implementacion TIMESTAMP,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    observaciones TEXT,
    FOREIGN KEY (investigacion_id) REFERENCES investigaciones(id)
);

-- Crear usuario administrador por defecto
INSERT INTO usuarios (nombre_usuario, contrasena, nombre_completo, correo, rol) 
VALUES ('admin', 'admin123', 'Administrador del Sistema', 'admin@ssst.com', 'ADMINISTRADOR')
ON CONFLICT (nombre_usuario) DO NOTHING;

-- Crear usuario responsable SST por defecto
INSERT INTO usuarios (nombre_usuario, contrasena, nombre_completo, correo, rol) 
VALUES ('responsable', 'resp123', 'Responsable de SST', 'responsable@ssst.com', 'RESPONSABLE_SST')
ON CONFLICT (nombre_usuario) DO NOTHING;

-- Indices para mejor rendimiento
CREATE INDEX IF NOT EXISTS idx_eventos_fecha ON eventos(fecha_hora);
CREATE INDEX IF NOT EXISTS idx_eventos_estado ON eventos(estado);
CREATE INDEX IF NOT EXISTS idx_eventos_tipo ON eventos(tipo);
CREATE INDEX IF NOT EXISTS idx_eventos_area ON eventos(area);
CREATE INDEX IF NOT EXISTS idx_investigaciones_evento ON investigaciones(evento_id);
CREATE INDEX IF NOT EXISTS idx_acciones_investigacion ON acciones_correctivas(investigacion_id);