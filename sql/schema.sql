-- Creacion de la base de datos si no existe
CREATE DATABASE IF NOT EXISTS agenda_citas_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE agenda_citas_db;

-- Tabla de Citas Medicas / Generales
CREATE TABLE IF NOT EXISTS citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_paciente VARCHAR(100) NOT NULL,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    hora_fin TIME NOT NULL,
    sala INT NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    estado ENUM('PENDIENTE', 'EN_CONSULTA', 'COMPLETADA', 'CANCELADA', 'EXPIRADA') DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de prueba variados

-- Citas en estado PENDIENTE (Jornada de hoy/futura)
INSERT INTO citas (nombre_paciente, fecha_cita, hora_cita, hora_fin, sala, motivo, estado) VALUES
('Carlos Gómez', CURRENT_DATE(), '08:00:00', '09:00:00', 1, 'Chequeo general de rutina', 'PENDIENTE'),
('María Fernández', CURRENT_DATE(), '09:00:00', '10:00:00', 1, 'Revisión de exámenes de laboratorio', 'PENDIENTE'),
('Luis Morales', CURRENT_DATE(), '08:00:00', '09:00:00', 2, 'Evaluación cardiología preventiva', 'PENDIENTE'),
('Ana Martínez', CURRENT_DATE(), '10:00:00', '11:00:00', 3, 'Consulta dermatológica por alergia', 'PENDIENTE');

-- Citas en estado EN_CONSULTA (Atención actual)
INSERT INTO citas (nombre_paciente, fecha_cita, hora_cita, hora_fin, sala, motivo, estado) VALUES
('Roberto Alvarado', CURRENT_DATE(), '11:00:00', '12:00:00', 1, 'Dolor lumbar agudo', 'EN_CONSULTA'),
('Sofía Ramírez', CURRENT_DATE(), '11:00:00', '12:00:00', 2, 'Seguimiento presión arterial', 'EN_CONSULTA');

-- Citas en estado COMPLETADA (Finalizadas con éxito)
INSERT INTO citas (nombre_paciente, fecha_cita, hora_cita, hora_fin, sala, motivo, estado) VALUES
('Elena Ríos', CURRENT_DATE() - INTERVAL 1 DAY, '08:00:00', '09:00:00', 1, 'Retiro de puntos quirúrgicos', 'COMPLETADA'),
('Diego Salazar', CURRENT_DATE() - INTERVAL 1 DAY, '10:00:00', '11:00:00', 2, 'Control endocrinología', 'COMPLETADA');

-- Citas en estado CANCELADA
INSERT INTO citas (nombre_paciente, fecha_cita, hora_cita, hora_fin, sala, motivo, estado) VALUES
('Javier Estrada', CURRENT_DATE(), '14:00:00', '15:00:00', 1, 'Cancelado por viaje de paciente', 'CANCELADA');

-- Citas que quedaron en el pasado y la app marcará/mostrará como EXPIRADA
INSERT INTO citas (nombre_paciente, fecha_cita, hora_cita, hora_fin, sala, motivo, estado) VALUES
('Lucía Méndez', CURRENT_DATE() - INTERVAL 2 DAY, '09:00:00', '10:00:00', 1, 'Consulta no asistida', 'EXPIRADA'),
('Fernando Ruiz', CURRENT_DATE() - INTERVAL 3 DAY, '15:00:00', '16:00:00', 3, 'Cita vencida', 'PENDIENTE');

SELECT * FROM citas;

